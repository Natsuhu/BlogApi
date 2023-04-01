package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.dto.admin.AdminArticleQueryDTO;
import com.natsu.blog.model.dto.admin.ArticleDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.HomeArticle;
import com.natsu.blog.model.vo.RandomArticle;
import com.natsu.blog.model.vo.ReadArticle;
import com.natsu.blog.model.vo.admin.AdminArticleTableItem;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {

    Map<?, ?> getArchives();

    ReadArticle getReadArticleById(int id);

    List<RandomArticle> getRandomArticles(int count);

    IPage<HomeArticle> getHomeArticles(BaseQueryDTO baseQueryDTO);

    IPage<HomeArticle> getArticlesByQueryParams(ArticleQueryDTO articleQueryDTO);

    void saveArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    IPage<AdminArticleTableItem> getArticleTable(AdminArticleQueryDTO queryDTO);
}
