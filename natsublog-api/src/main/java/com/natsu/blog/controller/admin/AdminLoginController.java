package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.entity.User;
import com.natsu.blog.service.UserService;
import com.natsu.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result Login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername())) {
            return Result.fail("用户名不能为空");
        }
        if (StrUtil.isBlank(user.getPassword())) {
            return Result.fail("密码不能为空");
        }
        try {
            User currentUser = userService.findUserByUsername(user.getUsername());
            if (!currentUser.getPassword().equals(user.getPassword())) {
                return Result.fail("用户名或密码错误");
            }
            String token = JwtUtils.createToken(currentUser.getUsername());
            return Result.success(token);
        } catch (Exception e) {
            return Result.fail("登录出错");
        }
    }

}
