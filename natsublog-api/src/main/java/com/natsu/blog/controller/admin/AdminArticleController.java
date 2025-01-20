package com.natsu.blog.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.annotation.OperationLogger;
import com.natsu.blog.enums.OperationTypeEnum;
import com.natsu.blog.model.dto.ArticleDTO;
import com.natsu.blog.model.dto.ArticleQueryDTO;
import com.natsu.blog.model.dto.Result;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文章管理控制层
 *
 * @author NatsuKaze
 * @since 2025/01/20
 */
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

    /**
     * 获取将要被更新的文章
     *
     * @description 用于从文章管理页面的编辑按钮跳转到写文章页面时，通过ID获取文章填充表单
     */
    @OperationLogger(type = OperationTypeEnum.QUERY, description = "博客管理-编辑")
    @PostMapping("/getUpdateArticle")
    public Result getUpdateArticle(@RequestParam("id") Long articleId) {
        try {
            ArticleDTO articleDTO = articleService.getUpdateArticle(articleId);
            return Result.success(articleDTO);
        } catch (Exception e) {
            log.error("获取更新文章失败，{}", e.getMessage());
            return Result.fail("获取更新文章G了：" + e.getMessage());
        }
    }

    /**
     * 获取文章表格
     *
     * @description 用于文章管理页面获取文章表格
     */
    @OperationLogger(type = OperationTypeEnum.QUERY, description = "博客管理")
    @PostMapping("/getArticleTable")
    public Result getArticleTable(@RequestBody ArticleQueryDTO queryDTO) {
        try {
            IPage<ArticleDTO> result = articleService.getArticleTable(queryDTO);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取文章表格失败，{}", e.getMessage());
            return Result.fail("获取文章表格G了：" + e.getMessage());
        }
    }

    /**
     * 保存文章
     */
    @Admin
    @OperationLogger(type = OperationTypeEnum.INSERT, description = "博客")
    @PostMapping("/saveArticle")
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
        //开始保存
        try {
            articleService.saveArticle(articleDTO);
            return Result.success("保存成功！");
        } catch (Exception e) {
            log.error("保存文章失败，{}", e.getMessage());
            return Result.fail("保存失败，" + e);
        }
    }

    /**
     * 更新文章
     */
    @Admin
    @OperationLogger(type = OperationTypeEnum.UPDATE, description = "博客")
    @PostMapping("/updateArticle")
    public Result updateArticle(@RequestBody ArticleDTO articleDTO) {
        //参数校验
        if (articleDTO.getId() == null) {
            return Result.fail("参数错误！必须填写文章ID");
        }
        //验证分类和标签存在
        Result checkExist = checkExist(articleDTO);
        if (checkExist != null) {
            return checkExist;
        }
        //开始更新
        try {
            articleService.updateArticle(articleDTO);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("文章更新失败！{}", e.getMessage());
            return Result.fail("文章更新失败！" + e);
        }
    }

    private Result checkParam(ArticleDTO articleDTO) {
        if (StringUtils.isEmpty(articleDTO.getTitle())) {
            return Result.fail("参数错误！文章必须包含标题");
        }
        if (StringUtils.isEmpty(articleDTO.getContent())) {
            return Result.fail("参数错误！文章必须包含正文内容");
        }
        if (articleDTO.getIsPublished() == null) {
            return Result.fail("参数错误！文章必须选择是否公开");
        }
        if (articleDTO.getIsRecommend() == null) {
            return Result.fail("参数错误！文章必须选择是否推荐");
        }
        if (articleDTO.getIsAppreciation() == null) {
            return Result.fail("参数错误！文章必须选择是否开启赞赏");
        }
        if (articleDTO.getIsCommentEnabled() == null) {
            return Result.fail("参数错误！文章必须选择是否开启评论");
        }
        if (articleDTO.getIsTop() == null) {
            return Result.fail("参数错误！文章必须选择是否置顶");
        }
        if (articleDTO.getCategoryId() == null) {
            return Result.fail("参数错误！文章必须属于一个分类");
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
