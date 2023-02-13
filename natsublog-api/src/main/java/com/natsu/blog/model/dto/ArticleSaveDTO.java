package com.natsu.blog.model.dto;

import com.natsu.blog.model.entity.Article;
import lombok.Data;

import java.util.List;

@Data
public class ArticleSaveDTO extends Article {

    private List<Long> tagIds;

}
