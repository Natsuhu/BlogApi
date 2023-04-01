package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("moment")
public class Moment implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 发表时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 点赞数
     */
    @TableField("`like`")
    private Long like;

    /**
     * 是否公开
     */
    @TableField("is_published")
    private Boolean isPublished;

}
