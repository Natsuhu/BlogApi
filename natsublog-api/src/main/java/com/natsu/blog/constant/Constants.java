package com.natsu.blog.constant;

/**
 * 全局常量
 *
 * @author NatsuKaze
 * @since 2023/1/19
 */
public class Constants {

    /**
     * 评论中QQ头像存储方式-本地
     */
    public static final String LOCAL = "local";

    /**
     * 评论中QQ头像存储方式-GitHub
     */
    public static final String GITHUB = "github";

    /**
     * 图片ContentType
     */
    public static final String IMAGE = "image";

    /**
     * 博客默认作者
     */
    public static final String DEFAULT_AUTHOR = "NatsuKaze";

    /**
     * 文章公开状态，公开
     */
    public static final Boolean PUBLISHED = true;

    /**
     * 文章公开状态，不公开
     */
    public static final Boolean NO_PUBLISHED = false;

    /**
     * 允许评论
     */
    public static final Boolean ALLOW_COMMENT = true;

    /**
     * 禁止评论
     */
    public static final Boolean FORBID_COMMENT = false;

    /**
     * 友情链接，审核
     */
    public static final Boolean AUDIT = true;

    /**
     * 友情链接，未审核
     */
    public static final Boolean NO_AUDIT = false;

    /**
     * 页面设置，LOGO和资料卡
     */
    public static final Integer PAGE_SETTING_LOGO = 1;

    /**
     * 页面设置，页脚
     */
    public static final Integer PAGE_SETTING_FOOTER = 2;

    /**
     * 页面设置，友链
     */
    public static final Integer PAGE_SETTING_FRIEND = 3;

    /**
     * 页面设置，关于我
     */
    public static final Integer PAGE_SETTING_ABOUT = 4;

    /**
     * 页面类型，阅读文章
     */
    public static final Integer PAGE_READ_ARTICLE = 0;

    /**
     * 页面类型，友链
     */
    public static final Integer PAGE_FRIEND = 1;

    /**
     * 页面类型，关于我
     */
    public static final Integer PAGE_ABOUT = 2;

    /**
     * 顶级评论的父ID
     */
    public static final Long TOP_COMMENT_PARENT_ID = -1L;

    /**
     * 标签默认颜色
     */
    public static final String TAG_DEFAULT_COLOR = "#000000";
}
