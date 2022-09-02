package com.natsu.blog.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Article {

	/**
	 * 文章ID
	 */
	private Integer id;

	/**
	 * 文章标题
	 */
	private String title;

	/**
	 * 略缩图地址
	 */
	private String thumbnail;

	/**
	 * 文章内容
	 */
	private String content;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 是否公开
	 */
	private Boolean isPublished;

	/**
	 * 是否推荐
	 */
	private Boolean isRecommend;

	/**
	 * 是否开启赞赏
	 */
	private Boolean isAppreciation;

	/**
	 * 是否允许评论
	 */
	private Boolean isCommentEnabled;

	/**
	 * 发表时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 查看数
	 */
	private Integer views;

	/**
	 * 字数
	 */
	private Integer words;

	/**
	 * 阅读时长
	 */
	private Integer readTime;

	/**
	 * 分类ID
	 */
	private Integer categoryId;

	/**
	 * 是否置顶
	 */
	private Boolean isTop;

	/**
	 * 作者名字
	 */
	private String authorName;

}
