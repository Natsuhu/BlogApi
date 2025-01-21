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
 * @Date 2022-10-22
 */
@Data
@Accessors(chain = true)
@TableName("visitor")
public class Visitor implements Serializable {

    /**
     * 访客标识码
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * ip
     */
    @TableField("ip")
    private String ip;

    /**
     * ip来源
     */
    @TableField("ip_source")
    private String ipSource;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

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
     * 访问页数统计
     */
    @TableField("pv")
    private Long pv;

    /**
     * 首次访问时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后访问时间
     */
    @TableField("last_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastTime;

    /**
     * user-agent用户代理
     */
    @TableField("user_agent")
    private String userAgent;

}
