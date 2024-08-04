package com.natsu.blog.enums;

public enum PageEnum {

    INDEX("index", 0),
    ARTICLE("article", 1),
    FRIEND("friend", 2),
    ABOUT("about", 3),
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
