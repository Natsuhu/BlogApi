package com.natsu.blog.controller.admin;

import com.natsu.blog.model.entity.Article;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/article")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/save")
    public Result saveArticle(@RequestBody Article article , @RequestBody List<Integer> tagIds) {
        try {
            articleService.saveArticle(article , tagIds);
            return Result.success("保存成功！");
        }catch (Exception e) {
            log.error("保存文章失败，{}" , e.getMessage());
            return Result.fail("保存失败");
        }
    }

}
