package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.ArticleMapper;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.entity.ArticleTagRef;
import com.natsu.blog.model.entity.Tag;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.ArticleTagRefService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import com.natsu.blog.service.async.AsyncTaskService;
import com.natsu.blog.utils.markdown.MarkdownUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        Map<String, List<ArticleDTO>> archivesMap = new LinkedHashMap<>();
        //先查询所有文章的创建日期
        List<String> archivesDate = articleMapper.getArchivesDate();
        //根据日期查询对应日期下的所有文章
        for (String date : archivesDate) {
            List<ArticleDTO> archives = articleMapper.getArchives(date);
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
    public ArticleDTO getReadArticle(Long articleId) {
        //获取文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublished, Constants.PUBLISHED);
        queryWrapper.eq(Article::getId, articleId);
        Article article = articleMapper.selectOne(queryWrapper);
        if (article != null) {
            //转DTO
            ArticleDTO articleDTO = new ArticleDTO();
            BeanUtils.copyProperties(article, articleDTO);
            //补充分类和标签
            String content = article.getContent();
            articleDTO.setContent(MarkdownUtils.markdownToHtml(content));
            articleDTO.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
            articleDTO.setTags(tagService.getTagsByArticleId(article.getId()));
            //更新文章阅读数量
            asyncTaskService.updateArticleViewCount(articleMapper, article);
            return articleDTO;
        }
        return null;
    }

    @Override
    public List<ArticleDTO> getRandomArticles(Integer count) {
        return articleMapper.getRandomArticles(count);
    }

    @Override
    public IPage<ArticleDTO> getArticles(ArticleQueryDTO queryCond) {
        //分页查询
        IPage<ArticleDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        IPage<ArticleDTO> articles = articleMapper.getArticles(page, queryCond);
        List<ArticleDTO> records = articles.getRecords();
        //补充标签
        for (ArticleDTO article : records) {
            article.setTags(tagService.getTagsByArticleId(article.getId()));
        }
        //封装结果
        IPage<ArticleDTO> pageResult = new Page<>(articles.getCurrent(), articles.getSize(), articles.getTotal());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public ArticleDTO getUpdateArticle(Long articleId) {
        //获取文章转DTO
        Article article = articleMapper.selectById(articleId);
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(article, articleDTO);
        //补充标签
        List<Tag> tags = tagService.getTagsByArticleId(articleId);
        List<Long> tagIds = tags.stream().map(Tag::getId).collect(Collectors.toList());
        articleDTO.setTagIds(tagIds);
        return articleDTO;
    }

    @Override
    public IPage<ArticleDTO> getArticleTable(ArticleQueryDTO queryCond) {
        IPage<ArticleDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return articleMapper.getArticleTable(page, queryCond);
    }

    @Transactional
    @Override
    public void saveArticle(ArticleDTO articleDTO) {
        //组装实体
        articleDTO.setId(null);
        if (articleDTO.getViews() == null) {
            articleDTO.setViews(Constants.COM_NUM_ZERO);
        }
        if (StringUtils.isBlank(articleDTO.getAuthorName())) {
            articleDTO.setAuthorName(Constants.DEFAULT_AUTHOR);
        }
        //先保存文章
        articleMapper.insert(articleDTO);
        //再保存文章标签引用
        List<Long> tagIds = articleDTO.getTagIds();
        if (CollectionUtils.isNotEmpty(tagIds)) {
            List<ArticleTagRef> tagRefs = new ArrayList<>(tagIds.size());
            for (Long tagId : tagIds) {
                tagRefs.add(new ArticleTagRef(articleDTO.getId(), tagId));
            }
            articleTagRefService.saveBatch(tagRefs);
        }
    }

    @Transactional
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        //先更新文章
        articleDTO.setUpdateTime(new Date());
        articleMapper.updateById(articleDTO);
        //标签对象为null不更新标签
        List<Long> updateTagIds = articleDTO.getTagIds();
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
    }

}
