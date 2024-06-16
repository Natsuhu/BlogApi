package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 操作日志实体对象
 *
 * @Author NatsuKaze
 * @Date 2024-06-16
 */

@Data
@Accessors(chain = true)
@TableName("operation_log")
public class OperationLog {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 接口
     */
    @TableField("uri")
    private String uri;

    /**
     * 方法
     */
    @TableField("method")
    private String method;

    /**
     * 参数
     */
    @TableField("param")
    private String param;

    /**
     * 操作类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 操作描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态 0失败 1成功
     */
    @TableField("status")
    private Integer status;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * IP归属地
     */
    @TableField("ip_source")
    private String ipSource;

    /**
     * 操作系统
     */
    @TableField("os")
    private String os;

    /**
     * 浏览器
     */
    @TableField("browser")
    private String browser;

    /**
     * 操作耗时
     */
    @TableField("times")
    private Integer times;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;

}
