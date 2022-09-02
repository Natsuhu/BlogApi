package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.mapper.CategoryMapper;
import com.natsu.blog.mapper.TagMapper;
import com.natsu.blog.model.params.PageParams;
import com.natsu.blog.model.vo.*;
import com.natsu.blog.pojo.Article;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.utils.ThreadUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ThreadUtils threadUtils;

    public Integer getPublicArticleCount(){
        return articleMapper.getPublicArticleCount();
    }

    public Map<String , Object> getArchives() {
        /*返回的结果集*/
        Map<String,Object> map = new HashMap<>();
        Map<String,List<Archives>> archivesMap = new LinkedHashMap<>();
        /*先查询所有文章的创建日期*/
        List<String> archivesDate = articleMapper.getArchivesDate();
        /*根据日期查询对应日期下的所有文章*/
        for(String date : archivesDate) {
            List<Archives> archives = articleMapper.getArchives(date);
            archivesMap.put(date,archives);
        }
        /*调用自己的方法获取文章数量*/
        int count = getPublicArticleCount();
        map.put("count",count);
        map.put("archives",archivesMap);
        return map;
    }

    public ArticleReadVO getArticleReadVOById(int id) {
        /*获取文章，并转为readVO*/
        Article article = articleMapper.getArticleById(id);
        if(article == null || !article.getIsPublished()){
            return null;
        }
        ArticleReadVO readVO = new ArticleReadVO();
        BeanUtils.copyProperties(article,readVO);
        /*补充readVO中缺少的tag和category*/
        readVO.setCategory(categoryMapper.getCategoryById(article.getCategoryId()));
        readVO.setTags(tagMapper.getTagsByArticleId(article.getId()));
        /*更新文章阅读数量*/
        threadUtils.updateArticleViewCount(articleMapper,article);
        return readVO;
    }

    public PageResult<HomeArticleList> getHomeArticleList(PageParams params) {
        /*分页查询article表*/
        Page<Article> page = new Page<>(params.getPage(),params.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublished,1);
        queryWrapper.orderByDesc(Article::getCreateTime);
        queryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> articlePage = articleMapper.selectPage(page,queryWrapper);
        /*获取查询结果，转为VO对象，并补充标签和分类*/
        List<Article> articleList = articlePage.getRecords();
        List<HomeArticleList> homeArticles = new ArrayList<>();
        for (Article article : articleList) {
            HomeArticleList homeArticle = new HomeArticleList();
            BeanUtils.copyProperties(article,homeArticle);
            homeArticle.setCategory(categoryMapper.getCategoryById(article.getCategoryId()));
            homeArticle.setTags(tagMapper.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }

         return new PageResult<>(page.getPages(), homeArticles);
    }

    public List<RandomArticles> getRandomArticles(int count) {
        return articleMapper.getRandomArticles(count);
    }

    public PageResult<HomeArticleList> getArticlesByCategoryId(PageParams params , int categoryId) {
        /*分页查询*/
        PageHelper.startPage(params.getPage(),params.getPageSize());
        List<Article> articles = articleMapper.getArticlesByCategoryId(categoryId);
        PageInfo<Article> pageInfo = new PageInfo<>(articles);
        List<HomeArticleList> homeArticles = new ArrayList<>();
        /*获取结果，遍历转VO对象并补充属性*/
        for(Article article : pageInfo.getList()) {
            HomeArticleList homeArticle = new HomeArticleList();
            BeanUtils.copyProperties(article,homeArticle);
            homeArticle.setCategory(categoryMapper.getCategoryById(article.getCategoryId()));
            homeArticle.setTags(tagMapper.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }
        return new PageResult<>(pageInfo.getPages(), homeArticles);
    }

    public PageResult<HomeArticleList> getArticlesByTagId(PageParams params , int tagId) {
        /*分页并获取结果*/
        PageHelper.startPage(params.getPage(),params.getPageSize());
        List<Article> articles = articleMapper.getArticlesByTagId(tagId);
        PageInfo<Article> pageInfo = new PageInfo<>(articles);
        List<HomeArticleList> homeArticles = new ArrayList<>();
        /*转VO对象并补充属性*/
        for(Article article : pageInfo.getList()) {
            HomeArticleList homeArticle = new HomeArticleList();
            BeanUtils.copyProperties(article,homeArticle);
            homeArticle.setCategory(categoryMapper.getCategoryById(article.getCategoryId()));
            homeArticle.setTags(tagMapper.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }
        return new PageResult<>(pageInfo.getPages(), homeArticles);
    }

    public Article getArticleById(int articleId) {
        return articleMapper.getArticleById(articleId);
    }

}
