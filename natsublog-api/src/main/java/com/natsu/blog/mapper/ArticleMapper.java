package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.model.vo.Archives;
import com.natsu.blog.model.vo.RandomArticles;
import com.natsu.blog.pojo.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    Integer getPublicArticleCount();

    List<String> getArchivesDate();

    List<Archives> getArchives(String date);

    Article getArticleById(int id);

    List<RandomArticles> getRandomArticles(int count);

    List<Article> getArticlesByCategoryId(int categoryId);

    List<Article> getArticlesByTagId(int tagId);
}
