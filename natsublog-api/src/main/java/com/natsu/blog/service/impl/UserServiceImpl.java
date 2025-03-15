package com.natsu.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.UserMapper;
import com.natsu.blog.model.dto.UserDTO;
import com.natsu.blog.model.dto.UserQueryDTO;
import com.natsu.blog.model.entity.User;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnnexService annexService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("密码错误");
        }
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    @Override
    public User findUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public IPage<UserDTO> getUserTable(UserQueryDTO queryCond) {
        IPage<UserDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        IPage<UserDTO> userTable = userMapper.getUserTable(page, queryCond);
        //处理头像
        List<UserDTO> records = userTable.getRecords();
        for (UserDTO userDTO : records) {
            userDTO.setAvatar(annexService.getAnnexAccessAddress(userDTO.getAvatar()));
        }
        //封装处理结果
        IPage<UserDTO> pageResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public void updatePassword(JSONObject userDTO) {
        String password = userDTO.getString("password");
        String encodePassword = new BCryptPasswordEncoder().encode(password);
        //打印密码到日志，免得改密码后忘了。
        log.info("更新密码：[{}]，加密密码：[{}]", password, encodePassword);
        //更新密码
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userDTO.getString("id"));
        updateWrapper.set(User::getPassword, encodePassword);
        update(updateWrapper);
    }
}
