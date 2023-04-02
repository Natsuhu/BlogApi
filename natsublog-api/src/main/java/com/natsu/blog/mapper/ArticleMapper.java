package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 获取归档日期
     */
    List<String> getArchivesDate();

    /**
     * 根据归档日期归档
     */
    List<ArticleDTO> getArchives(String date);

    /**
     * 获取随机文章
     */
    List<ArticleDTO> getRandomArticles(Integer count);

    /**
     * 博客前台--条件查询
     */
    IPage<ArticleDTO> getArticles(IPage<Article> page, @Param("queryCond") ArticleQueryDTO queryCond);

    /**
     * 后台管理系统--文章Table
     */
    IPage<ArticleDTO> getArticleTable(IPage<ArticleDTO> page, @Param("queryCond") ArticleQueryDTO queryCond);
}
