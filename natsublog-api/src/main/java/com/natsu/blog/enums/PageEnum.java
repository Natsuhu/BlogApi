package com.natsu.blog.enums;

/**
 * 页面类型枚举
 *
 * @author NatsuKaze
 * @since 2025/01/20
 */
public enum PageEnum {

    /**
     * 首页
     */
    INDEX("index", 0),
    /**
     * 阅读文章页面
     */
    ARTICLE("article", 1),
    /**
     * 友情链接页面
     */
    FRIEND("friend", 2),
    /**
     * 关于我页面
     */
    ABOUT("about", 3),
    /**
     * 登录页面
     */
    LOGIN("login", 4);

    /**
     * 页面名称
     */
    private final String pageName;

    /**
     * 页面编号
     */
    private final Integer pageCode;

    /**
     * 构造方法
     *
     * @param pageName 页面名称
     * @param pageCode 页面编号
     */
    PageEnum(String pageName, Integer pageCode) {
        this.pageName = pageName;
        this.pageCode = pageCode;
    }

    /**
     * 获取页面名称
     *
     * @return String
     */
    public String getPageName() {
        return pageName;
    }

    /**
     * 获取页面编号
     *
     * @return Integer
     */
    public Integer getPageCode() {
        return pageCode;
    }

}
