package com.natsu.blog.controller;


import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.CommentService;
import com.natsu.blog.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/about")
public class AboutController {

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public Result getAboutPageSetting() {
        Map settings = siteSettingService.getPageSetting(4);
        return Result.success(settings);
    }

    @GetMapping("/comments")
    public Result getAboutPageComments() {
        Map comments = commentService.getCommentsVOByPage(2);
        return Result.success(comments);
    }

}
