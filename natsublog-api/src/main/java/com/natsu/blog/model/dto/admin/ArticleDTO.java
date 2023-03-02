package com.natsu.blog.model.dto.admin;

import com.natsu.blog.model.entity.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文章DTO，用于保存和更新
 * */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleDTO extends Article {

    private List<Long> tagIds;

}
