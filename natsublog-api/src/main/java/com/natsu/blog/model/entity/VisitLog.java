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
@TableName( "visit_log" )
public class VisitLog implements Serializable {

	/**
	 * 访客标识码
	 */
   	@TableId(value = "uuid" , type = IdType.ASSIGN_UUID)
	private String uuid;

	/**
	 * 请求接口
	 */
   	@TableField( "uri" )
	private String uri;

	/**
	 * 请求方式
	 */
   	@TableField( "method" )
	private String method;

	/**
	 * 请求参数
	 */
   	@TableField( "param" )
	private String param;

	/**
	 * 访问行为
	 */
   	@TableField( "behavior" )
	private String behavior;

	/**
	 * 访问内容
	 */
   	@TableField( "content" )
	private String content;

	/**
	 * 备注
	 */
   	@TableField( "remark" )
	private String remark;

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
	 * 请求耗时（毫秒）
	 */
   	@TableField( "times" )
	private Integer times;

	/**
	 * 访问时间
	 */
   	@TableField( value = "create_time" , fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * user-agent用户代理
	 */
   	@TableField( "user_agent" )
	private String userAgent;

}
