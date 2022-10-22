package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description  
 * @Author  NatsuKaze
 * @Date 2022-10-22 
 */

@Data
@Accessors(chain = true)
@TableName( "visit_record" )
public class VisitRecord implements Serializable {

   	@TableId(value = "id" , type = IdType.AUTO)
	private Long id;

	/**
	 * 访问量
	 */
   	@TableField( "pv" )
	private Long pv;

	/**
	 * 独立用户
	 */
   	@TableField( "uv" )
	private Long uv;

	/**
	 * 日期"02-23"
	 */
   	@TableField( "date" )
	private String date;

}
