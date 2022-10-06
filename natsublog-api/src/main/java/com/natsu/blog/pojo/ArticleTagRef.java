package com.natsu.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description  
 * @Author  Hunter
 * @Date 2022-10-06 
 */

@Data
@Accessors(chain = true)
@TableName( "article_tag_ref" )
public class ArticleTagRef implements Serializable {

	/**
	 * 文章ID
	 */
   	@TableId( value = "id" , type = IdType.AUTO )
	private Integer articleId;

	/**
	 * 标签ID
	 */
   	@TableField( "tag_id" )
	private Long tagId;

}
