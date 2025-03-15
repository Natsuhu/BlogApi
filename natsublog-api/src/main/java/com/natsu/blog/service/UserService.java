package com.natsu.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.UserDTO;
import com.natsu.blog.model.dto.UserQueryDTO;
import com.natsu.blog.model.entity.User;

public interface UserService extends IService<User> {

    User findUserByUsername(String username);

    User findUserById(Long id);

    User findUserByUsernameAndPassword(String username, String password);

    IPage<UserDTO> getUserTable(UserQueryDTO queryCond);

    void updatePassword(JSONObject userDTO);
}
