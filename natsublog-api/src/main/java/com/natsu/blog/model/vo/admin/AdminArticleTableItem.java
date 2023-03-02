package com.natsu.blog.model.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AdminArticleTableItem {

    /**
     * ID
     * */
    private Long id;

    /**
     * 标题
     * */
    private String title;

    /**
     * 分类名
     * */
    private String categoryName;

    /**
     * 是否公开
     */
    private Boolean isPublished;

    /**
     * 是否推荐
     */
    private Boolean isRecommend;

    /**
     * 是否开启赞赏
     */
    private Boolean isAppreciation;

    /**
     * 是否允许评论
     */
    private Boolean isCommentEnabled;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 发表时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date updateTime;

    /**
     * 查看数
     */
    private Integer views;

    /**
     * 字数
     */
    private Integer words;

}
