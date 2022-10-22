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
 * @Date 2022-10-06 
 */

@Data
@Accessors(chain = true)
@TableName( "site_setting" )
public class SiteSetting implements Serializable {

	/**
	 * ID
	 */
   	@TableId( value = "id" , type = IdType.AUTO )
	private Integer id;

   	@TableField( "name_en" )
	private String nameEn;

   	@TableField( "name_zh" )
	private String nameZh;

   	@TableField( "content" )
	private String content;

	/**
	 * 1资料卡和logo，2页脚，3友链，4关于我
	 */
   	@TableField( "page" )
	private Long page;

}
