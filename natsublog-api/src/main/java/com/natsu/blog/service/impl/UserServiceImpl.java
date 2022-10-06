package com.natsu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.model.params.LoginParams;
import com.natsu.blog.model.vo.UserVO;
import com.natsu.blog.mapper.UserMapper;
import com.natsu.blog.pojo.User;
import com.natsu.blog.service.UserService;
import com.natsu.blog.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , User> implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    public User getUserById(int id) {
        return null;
    }

    public User getUserByEmailAndPsd(String email,String psd) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserEmail,email);
        queryWrapper.eq(User::getUserPass,psd);
        queryWrapper.last("limit 1");
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    public User getUserByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserEmail,email);
        queryWrapper.last("limit 1");
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    public Result getUserByToken(String token) {
        if(StringUtils.isBlank(token)){
            return Result.fail(-1000,"token为空");
        }
        Map<String,Object> map = JWTUtils.checkToken(token);
        if(map == null) {
            return Result.fail(-1001,"token解析失败");
        }
        String userJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJSON)) {
            return Result.fail(-1001,"token解析失败");
        }
        User user = JSON.parseObject(userJSON,User.class);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(userVO);
    }

    public Result login(LoginParams loginParams) {
        //获取用户名密码，为获取到则返回参数为空
        String email = loginParams.getAccount();
        String password = loginParams.getPassword();
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            return Result.fail(9001,"参数为空");
        }
        //通过email和密码查询用户
        User user = getUserByEmailAndPsd(email,password);
        if (user == null) {
            return Result.fail(9002,"用户名或密码错误");
        }
        //判读用户是否禁封
        if (user.getUserStatus() == 0) {
            return Result.fail(9003,"此账号禁封中");
        }
        //查到该用户后给该用户创建一个token并保存在redis中
        String token = JWTUtils.createToken(user.getUserId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    public Result register(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();

        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(9000,"参数为空");
        }
        User user = getUserByEmail(account);
        if (user != null) {
            return Result.fail(9000,"用户已经存在");
        }

        user = new User();
        user.setUserEmail(account);
        user.setUserPass(password);
        user.setUserRegisterTime(new Date());
        user.setUserStatus(1);
        user.setUserRole("user");
        insertUser(user);

        User newUser = getUserByEmail(account);
        String token = JWTUtils.createToken(newUser.getUserId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(newUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

}
