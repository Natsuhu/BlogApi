package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.Archives;
import com.natsu.blog.model.vo.RandomArticles;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    List<String> getArchivesDate();

    List<Archives> getArchives(String date);

    List<RandomArticles> getRandomArticles(int count);

    IPage<Article> getArticlesByQueryParams(IPage<Article> page, @Param("articleQueryDTO") ArticleQueryDTO articleQueryDTO);
}
