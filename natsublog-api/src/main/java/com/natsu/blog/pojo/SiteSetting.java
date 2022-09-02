package com.natsu.blog.pojo;

import lombok.Data;

@Data
public class SiteSetting {

	private Integer id;

	private String nameEn;

	private String nameZh;

	private String content;

	/**
	 * 1资料卡和logo，2页脚，3友链，4关于我
	 */
	private Integer page;

}
