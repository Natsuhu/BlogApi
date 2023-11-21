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
import com.natsu.blog.model.entity.ArticleTag;
import com.natsu.blog.model.entity.Tag;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.ArticleTagService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import com.natsu.blog.service.async.AsyncTaskService;
import com.natsu.blog.utils.CommonUtils;
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
    private ArticleTagService articleTagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AnnexService annexService;

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
        List<ArticleDTO> randomArticles = articleMapper.getRandomArticles(count);
        for (ArticleDTO articleDTO : randomArticles) {
            articleDTO.setThumbnail(annexService.getAnnexAccessAddress(articleDTO.getThumbnail()));
        }
        return randomArticles;
    }

    @Override
    public IPage<ArticleDTO> getArticles(ArticleQueryDTO queryCond) {
        //分页查询
        IPage<ArticleDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        IPage<ArticleDTO> articles = articleMapper.getArticles(page, queryCond);
        List<ArticleDTO> records = articles.getRecords();
        //补充标签和缩略图
        for (ArticleDTO article : records) {
            article.setThumbnail(annexService.getAnnexAccessAddress(article.getThumbnail()));
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
        //补充缩略图
        //articleDTO.setThumbnail(annexService.getAnnexAccessAddress(article.getThumbnail()));
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
        if (articleDTO.getEditTime() == null) {
            articleDTO.setEditTime(new Date());
        }
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
            List<ArticleTag> tagRefs = new ArrayList<>(tagIds.size());
            for (Long tagId : tagIds) {
                tagRefs.add(new ArticleTag(articleDTO.getId(), tagId));
            }
            articleTagService.saveBatch(tagRefs);
        }
    }

    @Transactional
    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        //若文章内容变化，则更新编辑时间
        Article dbArticle = articleMapper.selectById(articleDTO);
        if (!CommonUtils.compareString(dbArticle.getContent(), articleDTO.getContent())) {
            articleDTO.setEditTime(new Date());
        }
        articleMapper.updateById(articleDTO);
        //标签对象为null不更新标签
        List<Long> updateTagIds = articleDTO.getTagIds();
        if (updateTagIds == null) {
            return;
        }
        //查询原有标签
        LambdaQueryWrapper<ArticleTag> articleTagQuery = new LambdaQueryWrapper<>();
        articleTagQuery.eq(ArticleTag::getArticleId, articleDTO.getId());
        List<Long> dbTagIds = articleTagService.list(articleTagQuery).stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        //取交集和差集
        Collection<?> intersection = CollectionUtils.intersection(dbTagIds, updateTagIds);
        Collection<?> removeTags = CollectionUtils.subtract(dbTagIds, intersection);
        Collection<?> addTags = CollectionUtils.subtract(updateTagIds, intersection);
        //移除旧标签引用
        if (!CollectionUtils.isEmpty(removeTags)) {
            LambdaQueryWrapper<ArticleTag> removeTagsRefWrapper = new LambdaQueryWrapper<>();
            removeTagsRefWrapper.eq(ArticleTag::getArticleId, articleDTO.getId());
            removeTagsRefWrapper.in(ArticleTag::getTagId, removeTags);
            articleTagService.remove(removeTagsRefWrapper);
        }
        //增加新标签引用
        if (!CollectionUtils.isEmpty(addTags)) {
            List<ArticleTag> articleTags = new ArrayList<>();
            for (Object tagIds : addTags) {
                articleTags.add(new ArticleTag(articleDTO.getId(), (Long) tagIds));
            }
            articleTagService.saveBatch(articleTags);
        }
    }

}
