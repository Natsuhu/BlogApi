package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("annex")
public class Annex implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("reflect_id")
    private String reflectId;

    @TableField("name")
    private String name;

    @TableField("suffix")
    private String suffix;

    @TableField("size")
    private Long size;

    @TableField("file_type")
    private String fileType;

    @TableField("is_published")
    private Boolean isPublished;

    @TableField("storage_type")
    private Integer storageType;

    @TableField("path")
    private String path;

    @TableField("extra")
    private String extra;

    @TableField("download_count")
    private Integer downloadCount;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
