package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description  文章实体类
 * @author  NatsuKaze
 * @date 2022-10-06
 */

@Data
@Accessors(chain = true)
@TableName( "article" )
public class Article implements Serializable {

	/**
	 * 文章ID
	 */
   	@TableId( value = "id" , type = IdType.AUTO )
	private Long id;

	/**
	 * 文章标题
	 */
   	@TableField( "title" )
	private String title;

	/**
	 * 略缩图地址
	 */
   	@TableField( "thumbnail" )
	private String thumbnail;

	/**
	 * 文章内容
	 */
   	@TableField( "content" )
	private String content;

	/**
	 * 描述
	 */
   	@TableField( "description" )
	private String description;

	/**
	 * 是否公开
	 */
   	@TableField( "is_published" )
	private Boolean isPublished;

	/**
	 * 是否推荐
	 */
   	@TableField( "is_recommend" )
	private Boolean isRecommend;

	/**
	 * 是否开启赞赏
	 */
   	@TableField( "is_appreciation" )
	private Boolean isAppreciation;

	/**
	 * 是否允许评论
	 */
   	@TableField( "is_comment_enabled" )
	private Boolean isCommentEnabled;

	/**
	 * 发表时间
	 */
   	@TableField( value = "create_time" , fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 更新时间
	 */
   	@TableField( value = "update_time" , fill = FieldFill.INSERT)
	private Date updateTime;

	/**
	 * 查看数
	 */
   	@TableField( "views" )
	private Integer views;

	/**
	 * 字数
	 */
   	@TableField( "words" )
	private Integer words;

	/**
	 * 阅读时长
	 */
   	@TableField( "read_time" )
	private Long readTime;

	/**
	 * 分类ID
	 */
   	@TableField( "category_id" )
	private Long categoryId;

	/**
	 * 是否置顶
	 */
   	@TableField( "is_top" )
	private Boolean isTop;

	/**
	 * 作者名字
	 */
   	@TableField( "author_name" )
	private String authorName;

}
