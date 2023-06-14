package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author NatsuKaze
 * @Date 2022-10-06
 */

@Data
@Accessors(chain = true)
@TableName("comment")
public class Comment implements Serializable {

    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 头像地址
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 评论时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 公开或隐藏
     */
    @TableField("is_published")
    private Boolean isPublished;

    /**
     * 是否管理员评论
     */
    @TableField("is_admin_comment")
    private Boolean isAdminComment;

    /**
     * 1文章2友链3关于我
     */
    @TableField("page")
    private Integer page;

    /**
     * 回复的人昵称
     */
    @TableField("reply_nickname")
    private String replyNickname;

    /**
     * 评论的文章ID
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 父论ID
     */
    @TableField("parent_comment_id")
    private Long parentCommentId;

    /**
     * 个人主页
     */
    @TableField("website")
    private String website;

    /**
     * QQ
     */
    @TableField("qq")
    private String qq;

    /**
     * 归属ID
     */
    @TableField("origin_id")
    private Long originId;

}
