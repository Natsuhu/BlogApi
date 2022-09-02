package com.natsu.blog.pojo;


import lombok.Data;

import java.util.Date;

@Data
public class Moment{


	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 发表时间
	 */
	private Date createTime;

	/**
	 * 点赞数
	 */
	private Integer like;

	/**
	 * 是否公开
	 */
	private Boolean isPublished;

}
