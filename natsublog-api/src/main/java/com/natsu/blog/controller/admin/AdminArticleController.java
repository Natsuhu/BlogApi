package com.natsu.blog.controller.admin;

import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.ArticleSaveDTO;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/article")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public Result saveArticle(@RequestBody ArticleSaveDTO articleSaveDTO) {
        //参数校验
        if (articleSaveDTO.getId() != null) {
            return Result.fail("参数错误！不要携带ID");
        }
        if (StringUtils.isEmpty(articleSaveDTO.getTitle()) || StringUtils.isEmpty(articleSaveDTO.getContent())) {
            return Result.fail("参数错误！文章必须包含标题和正文内容");
        }
        if (articleSaveDTO.getIsPublished() == null || articleSaveDTO.getIsRecommend() == null) {
            return Result.fail("参数错误！文章必须选择是否公开和是否推荐");
        }
        if (articleSaveDTO.getIsAppreciation() == null || articleSaveDTO.getIsCommentEnabled() == null) {
            return Result.fail("参数错误！文章必须选择是否开启赞赏和是否开启评论");
        }
        if (articleSaveDTO.getCategoryId() == null || articleSaveDTO.getIsTop() == null) {
            return Result.fail("参数错误！文章必须属于一个分类且必须选择是否置顶");
        }
        //验证分类和标签存在
        if (categoryService.getById(articleSaveDTO.getCategoryId()) == null) {
            return Result.fail("所选分类不存在！");
        }
        List<Long> tagIds = articleSaveDTO.getTagIds();
        if (tagIds != null && tagIds.size() > 0) {
            if (tagService.listByIds(tagIds).size() != tagIds.size()) {
                return Result.fail("所选标签不存在！");
            }
        }
        //配置参数
        if (articleSaveDTO.getViews() == null) {
            articleSaveDTO.setViews(0);
        }
        if (StringUtils.isEmpty(articleSaveDTO.getAuthorName())) {
            articleSaveDTO.setAuthorName(Constants.DEFAULT_AUTHOR);
        }
        //开始保存
        try {
            articleService.saveArticle(articleSaveDTO);
            return Result.success("保存成功！");
        }catch (Exception e) {
            log.error("保存文章失败，{}" , e.getMessage());
            return Result.fail("保存失败，" + e);
        }
    }

}
