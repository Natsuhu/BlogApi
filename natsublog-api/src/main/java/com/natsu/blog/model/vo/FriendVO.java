package com.natsu.blog.model.vo;

import lombok.Data;

@Data
public class FriendVO {

    /**
     * id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 签名
     */
    private String description;

    /**
     * 网站
     */
    private String website;

    /**
     * 头像
     */
    private String avatar;

}
