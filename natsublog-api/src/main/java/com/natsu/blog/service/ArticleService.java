package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {

    Map<?, ?> getArchives();

    List<ArticleDTO> getRandomArticles(Integer count);

    IPage<ArticleDTO> getArticles(ArticleQueryDTO queryCond);

    ArticleDTO getReadArticle(Long articleId);

    ArticleDTO getUpdateArticle(Long articleId);

    void saveArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    IPage<ArticleDTO> getArticleTable(ArticleQueryDTO queryCond);
}
