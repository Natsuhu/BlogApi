package com.natsu.blog.controller;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.model.params.LoginParams;
import com.natsu.blog.service.UserService;
import com.natsu.blog.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("test")
    public Result test() {
        //从ThreadLocal里面获取用户信息
        System.out.println(UserThreadLocal.get());
        return Result.success(null);
    }

    @GetMapping("login")
    public Result login(@RequestBody LoginParams loginParams){
        return userService.login(loginParams);
    }

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return userService.getUserByToken(token);
    }

    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token){
        return userService.logout(token);
    }

    @GetMapping("register")
    public Result register(@RequestBody LoginParams loginParams) {
        return userService.register(loginParams);
    }
}
