package com.natsu.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryDTO extends BaseQueryDTO{

    private Integer categoryId;

    private List<Integer> tagIds;

    private Boolean isPublished;

}
