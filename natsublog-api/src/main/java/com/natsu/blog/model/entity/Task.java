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

/**
 * @author NatsuKaze
 * @description 定时任务实体类
 * @date 2024-08-23
 */
@Data
@Accessors(chain = true)
@TableName("task")
public class Task implements Serializable {

    /**
     * 主键JobKey
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 任务名称
     */
    @TableField("name")
    private String name;

    /**
     * 任务类型
     */
    @TableField("type")
    private Integer type;

    /**
     * cron表达式
     */
    @TableField("cron")
    private String cron;

    /**
     * 输入参数
     */
    @TableField("param")
    private String param;

    /**
     * 执行目标
     */
    @TableField("content")
    private String content;

    /**
     * 任务描述
     */
    @TableField("description")
    private String description;

    /**
     * 任务状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 当前执行次数
     */
    @TableField("current_count")
    private Integer currentCount;

    /**
     * 最大执行次数
     */
    @TableField("max_count")
    private Integer maxCount;

    /**
     * 下次执行时间
     */
    @TableField("next_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextTime;

    /**
     * 任务过期时间
     */
    @TableField("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 创建时间
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

}
