package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryDTO extends BaseQueryDTO{

    private Long categoryId;

    private List<Long> tagIds;

    private Boolean isPublished;

}
