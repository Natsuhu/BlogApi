package com.natsu.blog.model.vo;

import lombok.Data;

@Data
public class UserVO {
    private Integer userId;

    private String userName;

    private String userNickname;

    private String userEmail;

    private String userAvatar;

    private Integer userStatus;
}
