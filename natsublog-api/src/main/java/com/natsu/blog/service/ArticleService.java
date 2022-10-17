package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.HomeArticles;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.model.vo.RandomArticles;
import com.natsu.blog.model.vo.ReadArticle;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {

    Map getArchives();

    ReadArticle getReadArticleById(int id);

    List<RandomArticles> getRandomArticles(int count);

    PageResult<HomeArticles> getHomeArticles(BaseQueryDTO baseQueryDTO);

    PageResult<HomeArticles> getArticlesByQueryParams(ArticleQueryDTO articleQueryDTO);
}
