package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.admin.AdminArticleQueryDTO;
import com.natsu.blog.model.dto.admin.ArticleDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.HomeArticle;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.model.vo.RandomArticle;
import com.natsu.blog.model.vo.ReadArticle;
import com.natsu.blog.model.vo.admin.AdminArticleTableItem;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {

    Map<? , ?> getArchives();

    ReadArticle getReadArticleById(int id);

    List<RandomArticle> getRandomArticles(int count);

    PageResult<HomeArticle> getHomeArticles(BaseQueryDTO baseQueryDTO);

    PageResult<HomeArticle> getArticlesByQueryParams(ArticleQueryDTO articleQueryDTO);

    Boolean saveArticle(ArticleDTO articleDTO);

    Boolean updateArticle(ArticleDTO articleDTO);

    PageResult<AdminArticleTableItem> getArticleTable(AdminArticleQueryDTO queryDTO);
}
