package com.natsu.blog.service;

import com.natsu.blog.model.params.PageParams;
import com.natsu.blog.model.vo.ArticleReadVO;
import com.natsu.blog.model.vo.HomeArticleList;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.model.vo.RandomArticles;
import com.natsu.blog.pojo.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    Integer getPublicArticleCount();

    Map getArchives();

    ArticleReadVO getArticleReadVOById(int id);

    PageResult<HomeArticleList> getHomeArticleList(PageParams params);

    List<RandomArticles> getRandomArticles(int count);

    PageResult<HomeArticleList> getArticlesByCategoryId(PageParams params , int categoryId);

    PageResult<HomeArticleList> getArticlesByTagId(PageParams params , int tagId);

    Article getArticleById(int articleId);
}
