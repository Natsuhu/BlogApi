package com.natsu.blog.service;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.model.params.LoginParams;
import com.natsu.blog.pojo.User;

public interface UserService {
    int insertUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    User getUserByEmailAndPsd(String email,String psd);

    Result getUserByToken(String token);

    Result login(LoginParams loginParams);

    Result logout(String token);

    Result register(LoginParams loginParams);
}
