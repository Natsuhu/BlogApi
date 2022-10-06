package com.natsu.blog.pojo;

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
@TableName( "moment" )
public class Moment implements Serializable {

	/**
	 * ID
	 */
   	@TableId( value = "id" , type = IdType.AUTO )
	private Integer id;

	/**
	 * 内容
	 */
   	@TableField( "content" )
	private String content;

	/**
	 * 发表时间
	 */
   	@TableField( "create_time" )
	private Date createTime;

	/**
	 * 点赞数
	 */
   	@TableField( "like" )
	private Long like;

	/**
	 * 是否公开
	 */
   	@TableField( "is_published" )
	private Boolean isPublished;

}
