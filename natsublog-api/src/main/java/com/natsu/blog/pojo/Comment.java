package com.natsu.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Comment{

	/**
	 * 评论ID
	 */
	@TableId(value = "id" , type = IdType.AUTO)
	private Integer id;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 头像地址
	 */
	private String avatar;

	/**
	 * 评论时间
	 */
	private Date createTime;

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 公开或隐藏
	 */
	private Boolean isPublished;

	/**
	 * 是否管理员评论
	 */
	private Boolean isAdminComment;

	/**
	 * 0文章1友链2关于我
	 */
	private Integer page;

	/**
	 * 回复的人昵称
	 */
	private String replayNickname;

	/**
	 * 评论的文章ID
	 */
	private Integer articleId;

	/**
	 * 父论ID
	 */
	private Integer parentCommentId;

	/**
	 * 个人主页
	 */
	private String website;

	/**
	 * QQ
	 */
	private String qq;

}
