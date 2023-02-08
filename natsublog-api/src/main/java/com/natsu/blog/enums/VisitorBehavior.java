package com.natsu.blog.enums;

/**
 * 访客行为枚举
 */
public enum VisitorBehavior {
    UNKNOWN("UNKNOWN", "UNKNOWN"),

    INDEX("访问页面", "首页"),
    ARCHIVE("访问页面", "归档"),
    MOMENT("访问页面", "动态"),
    FRIEND("访问页面", "友链"),
    ABOUT("访问页面", "关于我"),

    ARTICLE("查看博客", ""),
    CATEGORY("点击分类", ""),
    TAG("点击标签", ""),
    SEARCH("搜索博客", ""),
    CLICK_FRIEND("点击友链", ""),
    LIKE_MOMENT("点赞动态", "");

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
     * */
    VisitorBehavior(String behavior, String content) {
        this.behavior = behavior;
        this.content = content;
    }

    /**
     * 获取行为
     * */
    public String getBehavior() {
        return behavior;
    }

    /**
     * 获取内容
     * */
    public String getContent() {
        return content;
    }
}
