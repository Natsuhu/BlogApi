package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.model.dto.LoginParams;
import com.natsu.blog.model.entity.User;

public interface UserService extends IService<User> {

    int insertUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    User getUserByEmailAndPsd(String email, String psd);

    Result getUserByToken(String token);

    Result login(LoginParams loginParams);

    Result logout(String token);

    Result register(LoginParams loginParams);
}
