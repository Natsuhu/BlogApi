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
 * @Description
 * @Author NatsuKaze
 * @Date 2022-10-06
 */
@Data
@Accessors(chain = true)
@TableName("setting")
public class Setting implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设置key
     */
    @TableField("setting_key")
    private String settingKey;

    /**
     * 设置value
     */
    @TableField("setting_value")
    private String settingValue;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 0首页，1文章，2友链，4关于
     */
    @TableField("page")
    private Long page;

    /**
     * 发表时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
