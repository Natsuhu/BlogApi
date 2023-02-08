package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.Archives;
import com.natsu.blog.model.vo.HomeArticles;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.model.vo.RandomArticles;
import com.natsu.blog.model.vo.ReadArticle;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper , Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Override
    public Map<String , Object> getArchives() {
        //返回的结果集
        Map<String,Object> map = new HashMap<>(2);
        Map<String,List<Archives>> archivesMap = new LinkedHashMap<>();
        //先查询所有文章的创建日期
        List<String> archivesDate = articleMapper.getArchivesDate();
        //根据日期查询对应日期下的所有文章
        for(String date : archivesDate) {
            List<Archives> archives = articleMapper.getArchives(date);
            archivesMap.put(date,archives);
        }
        //获取文章数量
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished, Constants.PUBLISHED);
        int count = articleMapper.selectCount(wrapper);
        map.put("count",count);
        map.put("archives",archivesMap);
        return map;
    }

    @Override
    public ReadArticle getReadArticleById(int id) {
        //获取文章
        Article article = articleMapper.selectById(id);
        if(article == null || !article.getIsPublished()){
            return null;
        }
        //补充readVO中缺少的tag和category
        ReadArticle readVO = new ReadArticle();
        BeanUtils.copyProperties(article,readVO);
        readVO.setCategory(categoryService.getById(article.getCategoryId()));
        readVO.setTags(tagService.getTagsByArticleId(article.getId()));
        //更新文章阅读数量
        updateArticleViewCount(article);
        return readVO;
    }

    @Override
    public List<RandomArticles> getRandomArticles(int count) {
        return articleMapper.getRandomArticles(count);
    }

    @Override
    public PageResult<HomeArticles> getHomeArticles(BaseQueryDTO params) {
        //分页查询article表
        IPage<Article> page = new Page<>(params.getPageNo() , params.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublished , Constants.PUBLISHED);
        queryWrapper.orderByDesc(Article::getCreateTime);
        queryWrapper.orderByDesc(Article::getIsTop);
        IPage<Article> articlePage = articleMapper.selectPage(page , queryWrapper);
        //获取查询结果，转为VO对象，并补充标签和分类
        List<Article> articleList = articlePage.getRecords();
        List<HomeArticles> homeArticles = new ArrayList<>();
        for (Article article : articleList) {
            HomeArticles homeArticle = new HomeArticles();
            BeanUtils.copyProperties(article,homeArticle);
            homeArticle.setCategory(categoryService.getById(article.getCategoryId()));
            homeArticle.setTags(tagService.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }
        return new PageResult<>(articlePage.getPages(), homeArticles);
    }

    @Override
    public PageResult<HomeArticles> getArticlesByQueryParams(ArticleQueryDTO articleQueryDTO) {
        //分页查询
        IPage<Article> page = new Page<>(articleQueryDTO.getPageNo() , articleQueryDTO.getPageSize());
        IPage<Article> articles = articleMapper.getArticlesByQueryParams(page , articleQueryDTO);
        List<HomeArticles> homeArticles = new ArrayList<>();
        //获取结果，遍历转VO对象并补充属性
        for(Article article : articles.getRecords()) {
            HomeArticles homeArticle = new HomeArticles();
            BeanUtils.copyProperties(article , homeArticle);
            homeArticle.setCategory(categoryService.getById(article.getCategoryId()));
            homeArticle.setTags(tagService.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }
        return new PageResult<>(articles.getPages(), homeArticles);
    }

    @Async("taskExecutor")
    public void updateArticleViewCount(Article article){
        int viewCount = article.getViews();
        Article articleUpdate = new Article();
        articleUpdate.setViews(viewCount+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //多设置一个，保证在多线程环境下，线程安全
        updateWrapper.eq(Article::getViews,viewCount);
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(2000);
            log.info(System.currentTimeMillis() + "更新文章阅读数完成");
        }catch (InterruptedException e){
            log.error("更新文章阅读数量失败：{}" , e.getMessage());
        }
    }
}
