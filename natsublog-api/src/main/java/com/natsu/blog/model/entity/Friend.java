package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 友情链接实体类
 *
 * @author NatsuKaze
 * @since 2023/2/12
 * */
@Data
@Accessors(chain = true)
@TableName( "friend" )
public class Friend {

    /**
     * id
     * */
    @TableId( value = "id" , type = IdType.AUTO )
    private Long id;

    /**
     * 昵称
     * */
    @TableField( value = "nickname")
    private String nickname;

    /**
     * 签名
     * */
    @TableField( value = "description")
    private String description;

    /**
     * 网站
     * */
    @TableField( value = "website")
    private String website;

    /**
     * 头像
     * */
    @TableField( value = "avatar")
    private String avatar;

    /**
     * 公开
     * */
    @TableField( value = "is_published")
    private Boolean isPublished;

    /**
     * 审核
     * */
    @TableField( value = "audit")
    private Boolean audit;

    /**
     * 点击次数
     * */
    @TableField( value = "click")
    private Integer click;

    /**
     * 创建时间
     * */
    @TableField( value = "create_time")
    private Date createTime;
}
