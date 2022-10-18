package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  Hunter
 * @Date 2022-10-06 
 */

@Data
@Accessors(chain = true)
@TableName( "tag" )
public class Tag implements Serializable {

	/**
	 * 标签ID
	 */
   	@TableId( value = "id" , type = IdType.AUTO )
	private Integer id;

	/**
	 * 标签名称
	 */
   	@TableField( "name" )
	private String name;

	/**
	 *  创建时间
	 */
	@TableField( "create_time" )
	private Date createTime;
}
