package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Article;
import lombok.Data;

import java.util.List;

@Data
public class ArticleDTO extends Article {

    private List<Long> tagIds;

}
