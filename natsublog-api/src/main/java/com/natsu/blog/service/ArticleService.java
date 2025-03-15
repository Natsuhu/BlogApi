package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {

    /**
     * 前台。获取文章归档
     *
     * @return Map
     */
    Map<?, ?> getArchives();

    /**
     * 前台。获取随机文章
     *
     * @param count 随机文章数量
     * @return List<ArticleDTO>
     */
    List<ArticleDTO> getRandomArticles(Integer count);

    /**
     * 前台。获取文章列表
     *
     * @param queryCond 查询条件
     * @return IPage<ArticleDTO>
     */
    IPage<ArticleDTO> getArticles(ArticleQueryDTO queryCond);

    /**
     * 前台。阅读文章
     *
     * @param articleId 文章ID
     * @return ArticleDTO
     */
    ArticleDTO getReadArticle(Long articleId);

    /**
     * 后台。获取将被更新的文章
     *
     * @param articleId 文章ID
     * @return ArticleDTO
     */
    ArticleDTO getUpdateArticle(Long articleId);

    /**
     * 后台。保存文章
     *
     * @param articleDTO articleDTO
     */
    Long saveArticle(ArticleDTO articleDTO);

    /**
     * 后台。更新文章
     *
     * @param articleDTO articleDTO
     */
    void updateArticle(ArticleDTO articleDTO);

    /**
     * 后台。获取文章表格
     *
     * @param queryCond 查询条件
     * @return IPage<ArticleDTO>
     */
    IPage<ArticleDTO> getArticleTable(ArticleQueryDTO queryCond);

    /**
     * 删除文章
     *
     * @param articleDTO articleDto
     */
    void deleteArticle(ArticleDTO articleDTO);
}
