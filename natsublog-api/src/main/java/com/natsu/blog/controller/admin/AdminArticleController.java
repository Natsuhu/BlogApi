package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.admin.AdminArticleQueryDTO;
import com.natsu.blog.model.dto.admin.ArticleDTO;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.model.vo.admin.AdminArticleTableItem;
import com.natsu.blog.service.ArticleService;
import com.natsu.blog.service.CategoryService;
import com.natsu.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

    @PostMapping("/getArticleTable")
    public Result getArticleTable(@RequestBody AdminArticleQueryDTO adminArticleQueryDTO) {
        IPage<AdminArticleTableItem> result = articleService.getArticleTable(adminArticleQueryDTO);
        return Result.success(result.getPages(), result.getTotal(), result.getRecords());
    }

    @PostMapping("/save")
    public Result saveArticle(@RequestBody ArticleDTO articleDTO) {
        //参数校验
        Result checkParam = checkParam(articleDTO);
        if (checkParam != null) {
            return checkParam;
        }
        //验证分类和标签存在
        Result checkExist = checkExist(articleDTO);
        if (checkExist != null) {
            return checkExist;
        }
        //配置参数
        if (articleDTO.getId() != null) {
            articleDTO.setId(null);
        }
        if (articleDTO.getViews() == null) {
            articleDTO.setViews(0);
        }
        if (StringUtils.isEmpty(articleDTO.getAuthorName())) {
            articleDTO.setAuthorName(Constants.DEFAULT_AUTHOR);
        }
        //开始保存
        try {
            articleService.saveArticle(articleDTO);
            return Result.success("保存成功！");
        }catch (Exception e) {
            log.error("保存文章失败，{}" , e.getMessage());
            return Result.fail("保存失败，" + e);
        }
    }

    @PostMapping("/update")
    public Result updateArticle(@RequestBody ArticleDTO articleDTO) {
        //参数校验
        if (articleDTO.getId() == null) {
            return Result.fail("参数错误！必须填写文章ID");
        }
        Result checkParam = checkParam(articleDTO);
        if (checkParam != null) {
            return checkParam;
        }
        //验证文章、标签、分类存在
        if (articleService.getById(articleDTO.getId()) == null) {
            return Result.fail("要修改的文章不存在！");
        }
        Result checkExist = checkExist(articleDTO);
        if (checkExist != null) {
            return checkExist;
        }
        //配置参数
        if (articleDTO.getViews() == null) {
            articleDTO.setViews(0);
        }
        if (StringUtils.isEmpty(articleDTO.getAuthorName())) {
            articleDTO.setAuthorName(Constants.DEFAULT_AUTHOR);
        }
        //开始更新
        try {
            articleService.updateArticle(articleDTO);
            return Result.success("更新成功");
        }catch (Exception e) {
            log.error("文章更新失败！{}",e.getMessage());
            return Result.fail("文章更新失败！" + e);
        }
    }

    private Result checkParam(ArticleDTO articleDTO) {
        if (StringUtils.isEmpty(articleDTO.getTitle()) || StringUtils.isEmpty(articleDTO.getContent())) {
            return Result.fail("参数错误！文章必须包含标题和正文内容");
        }
        if (articleDTO.getIsPublished() == null || articleDTO.getIsRecommend() == null) {
            return Result.fail("参数错误！文章必须选择是否公开和是否推荐");
        }
        if (articleDTO.getIsAppreciation() == null || articleDTO.getIsCommentEnabled() == null) {
            return Result.fail("参数错误！文章必须选择是否开启赞赏和是否开启评论");
        }
        if (articleDTO.getCategoryId() == null || articleDTO.getIsTop() == null) {
            return Result.fail("参数错误！文章必须属于一个分类且必须选择是否置顶");
        }
        return null;
    }

    private Result checkExist(ArticleDTO articleDTO) {
        if (categoryService.getById(articleDTO.getCategoryId()) == null) {
            return Result.fail("所选分类不存在！");
        }
        List<Long> tagIds = articleDTO.getTagIds();
        if (!CollectionUtils.isEmpty(tagIds)) {
            if (tagService.listByIds(tagIds).size() != tagIds.size()) {
                return Result.fail("所选标签不存在！");
            }
        }
        return null;
    }

}
