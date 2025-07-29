package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("music")
public class Music {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("title")
    private String title;

    @TableField("author")
    private String author;

    @TableField("catalog_id")
    private String catalogId;

    @TableField("music_url")
    private String musicUrl;

    @TableField("lrc_url")
    private String lrcUrl;

    @TableField("pic_url")
    private String picUrl;

    @TableField("is_published")
    private Boolean isPublished;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
