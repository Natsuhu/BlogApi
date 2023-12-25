package com.natsu.blog.constant;

/**
 * 全局常量
 *
 * @author NatsuKaze
 * @since 2023/1/19
 */
public class Constants {

    /**
     * 通用INT 0
     */
    public static final Integer COM_NUM_ZERO = 0;

    /**
     * 文件（读写）缓存大小
     */
    public static final Integer FILE_BUFFER_SIZE = 2097152;

    /**
     * 顶级评论的父ID
     */
    public static final Long TOP_COMMENT_PARENT_ID = -1L;

    /**
     * 公开状态，公开
     */
    public static final Boolean PUBLISHED = true;

    /**
     * 公开状态，不公开
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
     * 设置值-博客名称
     */
    public static final String SETTING_KEY_BLOG_NAME = "blogName";

    /**
     * 设置值-网页标题后缀
     */
    public static final String SETTING_KEY_WEB_TITLE_SUFFIX = "webTitleSuffix";

    /**
     * 设置值-资料卡头像
     */
    public static final String SETTING_KEY_CARD_AVATAR = "cardAvatar";

    /**
     * 设置值-资料卡名字
     */
    public static final String SETTING_KEY_CARD_NAME = "cardName";

    /**
     * 设置值-资料卡个性签名
     */
    public static final String SETTING_KEY_CARD_SIGNATURE = "cardSignature";

    /**
     * 设置值-友情链接文案
     */
    public static final String SETTING_KEY_FRIEND_CONTENT = "friendContent";

    /**
     * 设置值-友情链接评论区开关
     */
    public static final String SETTING_KEY_FRIEND_IS_COMMENT = "friendIsComment";

    /**
     * 设置值-关于我页面音乐ID
     */
    public static final String SETTING_KEY_ABOUT_MUSIC_ID = "aboutMusicId";

    /**
     * 设置值-关于我页面文案
     */
    public static final String SETTING_KEY_ABOUT_CONTENT = "aboutContent";

    /**
     * 设置值-关于我页面评论区开关
     */
    public static final String SETTING_KEY_ABOUT_IS_COMMENT = "aboutIsComment";

    /**
     * 设置值-关于我页面音乐服务器
     */
    public static final String SETTING_KEY_ABOUT_MUSIC_SERVER = "aboutMusicServer";

    /**
     * 设置值-关于我页面标题
     */
    public static final String SETTING_KEY_ABOUT_TITLE = "aboutTitle";

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
     * 标签默认颜色
     */
    public static final String TAG_DEFAULT_COLOR = "#000000";

    /**
     * QQ头像名称
     */
    public static final String FILE_NAME_QQ_AVATAR = "qq_avatar";

    /**
     * 路径
     */
    public static final String PATH = "path";

    /**
     * CONTENT_DISPOSITION - 附件（在浏览器下载）
     */
    public static final String CONTENT_DISPOSITION_ANNEX = "attachment";

    /**
     * CONTENT_DISPOSITION - 内嵌（在浏览器内嵌显示）
     */
    public static final String CONTENT_DISPOSITION_INLINE = "inline";

    /**
     * 文件扩展名 - PDF
     */
    public static final String FILE_EXTENSION_PDF = "pdf";

    /**
     * 文件扩展名 - IMAGE
     */
    public static final String[] FILE_EXTENSION_IMG = {"jpg", "jpeg", "bmp", "png", "gif", "wbmp", "webp", "tiff"};

}
