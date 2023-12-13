package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文章查询DTO，后台
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryDTO extends BaseQueryDTO {

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;

}


