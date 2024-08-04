package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.User;

public interface UserService extends IService<User> {

    User findUserByUsername(String username);

    User findUserById(Long id);

    User findUserByUsernameAndPassword(String username, String password);

}
