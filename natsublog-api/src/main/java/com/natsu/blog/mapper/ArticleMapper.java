package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.admin.AdminArticleQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.Archives;
import com.natsu.blog.model.vo.RandomArticle;
import com.natsu.blog.model.vo.admin.AdminArticleTableItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 获取归档日期
     * */
    List<String> getArchivesDate();

    /**
     * 根据归档日期归档
     * */
    List<Archives> getArchives(String date);

    /**
     * 获取随机文章
     * */
    List<RandomArticle> getRandomArticles(int count);

    /**
     * 根据查询参数分页查询
     * */
    IPage<Article> getArticlesByQueryParams(IPage<Article> page, @Param("articleQueryDTO") ArticleQueryDTO articleQueryDTO);

    /**
     * 后台管理系统--文章Table
     * */
    IPage<AdminArticleTableItem> getArticleTable(IPage<AdminArticleTableItem> page , @Param("queryParam") AdminArticleQueryDTO queryParam);
}
