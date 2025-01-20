package com.natsu.blog.enums;

/**
 * 访客行为枚举
 */
public enum VisitorBehavior {
    /**
     * 未知行为
     */
    UNKNOWN("UNKNOWN", "UNKNOWN"),

    /**
     * 访问主页
     */
    INDEX("访问页面", "首页"),
    /**
     * 访问文章归档页面
     */
    ARCHIVE("访问页面", "归档"),
    /**
     * 访问动态页面
     */
    MOMENT("访问页面", "动态"),
    /**
     * 访问友情链接页面
     */
    FRIEND("访问页面", "友链"),
    /**
     * 访问关于我页面
     */
    ABOUT("访问页面", "关于我"),

    /**
     * 阅读文章
     */
    ARTICLE("查看博客", ""),
    /**
     * 点击分类
     */
    CATEGORY("点击分类", ""),
    /**
     * 点击标签
     */
    TAG("点击标签", ""),
    /**
     * 搜索博客
     */
    SEARCH("搜索博客", ""),
    /**
     * 点击友链
     */
    CLICK_FRIEND("点击友链", ""),
    /**
     * 点赞动态
     */
    LIKE_MOMENT("点赞动态", ""),
    /**
     * 发表评论
     */
    COMMENT("发表评论", "");

    /**
     * 行为
     */
    private final String behavior;

    /**
     * 内容
     */
    private final String content;

    /**
     * 构造方法
     */
    VisitorBehavior(String behavior, String content) {
        this.behavior = behavior;
        this.content = content;
    }

    /**
     * 获取行为
     */
    public String getBehavior() {
        return behavior;
    }

    /**
     * 获取内容
     */
    public String getContent() {
        return content;
    }
}
