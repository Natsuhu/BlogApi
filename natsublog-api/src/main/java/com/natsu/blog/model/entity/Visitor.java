package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  NatsuKaze
 * @Date 2022-10-22 
 */

@Data
@Accessors(chain = true)
@TableName( "visitor" )
public class Visitor implements Serializable {

	/**
	 * 访客标识码
	 */
	@TableId(value = "uuid" , type = IdType.ASSIGN_UUID)
	private String uuid;

	/**
	 * ip
	 */
   	@TableField( "ip" )
	private String ip;

	/**
	 * ip来源
	 */
   	@TableField( "ip_source" )
	private String ipSource;

	/**
	 * 操作系统
	 */
   	@TableField( "os" )
	private String os;

	/**
	 * 浏览器
	 */
   	@TableField( "browser" )
	private String browser;

	/**
	 * 首次访问时间
	 */
   	@TableField( value = "create_time" , fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 最后访问时间
	 */
   	@TableField( "last_time" )
	private Date lastTime;

	/**
	 * 访问页数统计
	 */
   	@TableField( "pv" )
	private Long pv;

	/**
	 * user-agent用户代理
	 */
   	@TableField( "user_agent" )
	private String userAgent;

}
