package com.natsu.blog.model.vo;

import lombok.Data;

/**
 * Setting表VO对象
 *
 * @author NK
 * @since 2023/12/07
 */
@Data
public class SettingVO {

    /**
     * 设置值-博客名称
     */
    private String blogName;

    /**
     * 设置值-网页标题后缀
     */
    private String webTitleSuffix;

    /**
     * 设置值-资料卡头像
     */
    private String cardAvatar;

    /**
     * 设置值-资料卡名字
     */
    private String cardName;

    /**
     * 设置值-资料卡个性签名
     */
    private String cardSignature;

    /**
     * 设置值-友情链接文案
     */
    private String friendContent;

    /**
     * 设置值-友情链接评论区开关
     */
    private String friendIsComment;

    /**
     * 设置值-关于我页面音乐ID
     */
    private String aboutMusicId;

    /**
     * 设置值-关于我页面文案
     */
    private String aboutContent;

    /**
     * 设置值-关于我页面评论区开关
     */
    private String aboutIsComment;

    /**
     * 设置值-关于我页面音乐服务器
     */
    private String aboutMusicServer;

    /**
     * 设置值-关于我页面标题
     */
    private String aboutTitle;

}
