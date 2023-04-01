package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.dto.admin.AdminArticleQueryDTO;
import com.natsu.blog.model.dto.admin.ArticleDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.ArticleTagRef;
import com.natsu.blog.model.vo.Archives;
import com.natsu.blog.model.vo.HomeArticle;
import com.natsu.blog.model.vo.RandomArticle;
import com.natsu.blog.model.vo.ReadArticle;
import com.natsu.blog.model.vo.admin.AdminArticleTableItem;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.ArticleTagRefService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import com.natsu.blog.service.async.AsyncTaskService;
import com.natsu.blog.utils.markdown.MarkdownUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagRefService articleTagRefService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Override
    public Map<String, Object> getArchives() {
        //返回的结果集
        Map<String, Object> map = new HashMap<>(2);
        Map<String, List<Archives>> archivesMap = new LinkedHashMap<>();
        //先查询所有文章的创建日期
        List<String> archivesDate = articleMapper.getArchivesDate();
        //根据日期查询对应日期下的所有文章
        for (String date : archivesDate) {
            List<Archives> archives = articleMapper.getArchives(date);
            archivesMap.put(date, archives);
        }
        //获取文章数量
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished, Constants.PUBLISHED);
        int count = articleMapper.selectCount(wrapper);
        map.put("count", count);
        map.put("archives", archivesMap);
        return map;
    }

    @Override
    public ReadArticle getReadArticleById(int id) {
        //获取文章
        Article article = articleMapper.selectById(id);
        if (article == null || !article.getIsPublished()) {
            return null;
        }
        //补充readVO中缺少的tag和category
        String content = article.getContent();
        ReadArticle readVO = new ReadArticle();
        BeanUtils.copyProperties(article, readVO);
        readVO.setContent(MarkdownUtils.markdownToHtml(content));
        readVO.setCategory(categoryService.getById(article.getCategoryId()));
        readVO.setTags(tagService.getTagsByArticleId(article.getId()));
        //更新文章阅读数量
        asyncTaskService.updateArticleViewCount(articleMapper, article);
        return readVO;
    }

    @Override
    public List<RandomArticle> getRandomArticles(int count) {
        return articleMapper.getRandomArticles(count);
    }

    @Override
    public IPage<HomeArticle> getHomeArticles(BaseQueryDTO params) {
        //分页查询article表
        IPage<Article> page = new Page<>(params.getPageNo(), params.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublished, Constants.PUBLISHED);
        //排序优先级：置顶--推荐--创建时间
        queryWrapper.orderByDesc(Article::getIsTop);
        queryWrapper.orderByDesc(Article::getIsRecommend);
        queryWrapper.orderByDesc(Article::getCreateTime);
        IPage<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        //获取查询结果，转为VO对象，并补充标签和分类
        List<Article> articleList = articlePage.getRecords();
        List<HomeArticle> homeArticles = new ArrayList<>();
        for (Article article : articleList) {
            HomeArticle homeArticle = new HomeArticle();
            BeanUtils.copyProperties(article, homeArticle);
            homeArticle.setCategory(categoryService.getById(article.getCategoryId()));
            homeArticle.setTags(tagService.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }
        //封装结果
        IPage<HomeArticle> pageResult = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        pageResult.setRecords(homeArticles);
        return pageResult;
    }

    @Override
    public IPage<HomeArticle> getArticlesByQueryParams(ArticleQueryDTO articleQueryDTO) {
        //分页查询
        IPage<Article> page = new Page<>(articleQueryDTO.getPageNo(), articleQueryDTO.getPageSize());
        IPage<Article> articles = articleMapper.getArticlesByQueryParams(page, articleQueryDTO);
        List<HomeArticle> homeArticles = new ArrayList<>();
        //获取结果，遍历转VO对象并补充属性
        for (Article article : articles.getRecords()) {
            HomeArticle homeArticle = new HomeArticle();
            BeanUtils.copyProperties(article, homeArticle);
            homeArticle.setCategory(categoryService.getById(article.getCategoryId()));
            homeArticle.setTags(tagService.getTagsByArticleId(article.getId()));
            homeArticles.add(homeArticle);
        }
        //封装结果
        IPage<HomeArticle> pageResult = new Page<>(articles.getCurrent(), articles.getSize(), articles.getTotal());
        pageResult.setRecords(homeArticles);
        return pageResult;
    }

    @Override
    public IPage<AdminArticleTableItem> getArticleTable(AdminArticleQueryDTO queryDTO) {
        IPage<AdminArticleTableItem> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        return articleMapper.getArticleTable(page, queryDTO);
    }

    @Transactional
    @Override
    public void saveArticle(ArticleDTO articleDTO) {
        //获取文章实体和标签IDS
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        List<Long> tagIds = articleDTO.getTagIds();
        try {
            //先保存文章
            articleMapper.insert(article);
            //再保存文章标签引用
            if (tagIds != null && tagIds.size() > 0) {
                List<ArticleTagRef> tagRefs = new ArrayList<>(tagIds.size());
                for (Long tagId : tagIds) {
                    tagRefs.add(new ArticleTagRef(article.getId(), tagId));
                }
                articleTagRefService.saveBatch(tagRefs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        //获取文章实体和标签IDS
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        List<Long> updateTagIds = articleDTO.getTagIds();
        try {
            //先更新文章
            article.setUpdateTime(new Date());
            articleMapper.updateById(article);
            //标签对象为null不更新标签
            if (updateTagIds == null) {
                return;
            }
            //查询原有标签
            LambdaQueryWrapper<ArticleTagRef> articleTagQuery = new LambdaQueryWrapper<>();
            articleTagQuery.eq(ArticleTagRef::getArticleId, articleDTO.getId());
            List<Long> dbTagIds = articleTagRefService.list(articleTagQuery).stream().map(ArticleTagRef::getTagId).collect(Collectors.toList());
            //取交集和差集
            Collection<?> intersection = CollectionUtils.intersection(dbTagIds, updateTagIds);
            Collection<?> removeTags = CollectionUtils.subtract(dbTagIds, intersection);
            Collection<?> addTags = CollectionUtils.subtract(updateTagIds, intersection);
            //移除旧标签引用
            if (!CollectionUtils.isEmpty(removeTags)) {
                LambdaQueryWrapper<ArticleTagRef> removeTagsRefWrapper = new LambdaQueryWrapper<>();
                removeTagsRefWrapper.eq(ArticleTagRef::getArticleId, articleDTO.getId());
                removeTagsRefWrapper.in(ArticleTagRef::getTagId, removeTags);
                articleTagRefService.remove(removeTagsRefWrapper);
            }
            //增加新标签引用
            if (!CollectionUtils.isEmpty(addTags)) {
                List<ArticleTagRef> articleTagRefs = new ArrayList<>();
                for (Object tagIds : addTags) {
                    articleTagRefs.add(new ArticleTagRef(articleDTO.getId(), (Long) tagIds));
                }
                articleTagRefService.saveBatch(articleTagRefs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
