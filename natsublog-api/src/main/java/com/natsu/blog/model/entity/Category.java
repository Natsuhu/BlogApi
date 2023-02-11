package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author  NatsuKaze
 * @Date 2022-10-06
 */

@Data
@Accessors(chain = true)
@TableName( "category" )
public class Category implements Serializable {

	/**
	 * 分类ID
	 */
   	@TableId( value = "id" , type = IdType.AUTO )
	private Long id;

	/**
	 * 分类名称
	 */
   	@TableField( "name" )
	private String name;

   	/**
	 *  创建时间
	 */
   	@TableField( value = "create_time" , fill = FieldFill.INSERT)
   	private Date createTime;
}
