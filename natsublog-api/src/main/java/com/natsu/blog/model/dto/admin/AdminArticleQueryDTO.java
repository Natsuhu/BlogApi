package com.natsu.blog.model.dto.admin;

import com.natsu.blog.model.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 文章查询DTO，后台
 * */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminArticleQueryDTO extends BaseQueryDTO {

    /**
     * 分类ID
     * */
    private Long categoryId;

    /**
     * 置顶
     * */
    private Boolean isTop;

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
     * 时间范围，开始时间
     * */
    private Date startTime;

    /**
     * 时间范围，结束时间
     * */
    private Date endTime;

}


