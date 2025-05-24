package com.natsu.blog.model.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    /**
     * 设置值--首页背景图标题
     */
    private String headerTitle;

    /**
     * 设置值--首页背景图地址
     */
    private String headerImage;

    /**
     * 设置值--全局背景图地址
     */
    private String bodyImage;

    /**
     * 设置值--资料卡Github
     */
    private String github;

    /**
     * 设置值--资料卡QQ
     */
    private String qq;

    /**
     * 设置值--资料卡B站
     */
    private String bilibili;

    /**
     * 设置值--资料卡网易云
     */
    private String netease;

    /**
     * 设置值--资料卡邮箱
     */
    private String email;

    /**
     * 设置值--博主评论标识
     */
    private String adminCommentLabel;

    /**
     * 设置值--博客前台登录页背景图
     */
    private String frontLoginImage;

    /**
     * 设置值--博客后台登录页背景图
     */
    private String backLoginImage;

    /**
     * 设置值--版权信息
     */
    private JSONObject copyright;

    /**
     * 设置值--ICP备案信息
     */
    private String icpInfo;

    /**
     * 设置值--徽标组
     */
    private JSONArray badgeList;

    /**
     * 设置值--资料卡自定义内容
     */
    private JSONArray cardCustom;

    /**
     * 设置值--删除文章时，是否顺带删除文章下的评论
     */
    private String isDeleteCommentInDeleteArticle;

    /**
     * 设置值--首图是否用Base64传输
     */
    private String isHeaderImageBase64;

    /**
     * 设置值--首图标题颜色
     */
    private String headerTitleColor;

}
