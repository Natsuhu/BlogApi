package com.natsu.blog.controller;

import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客前台，标签接口
 *
 * @author NatsuKaze
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    /**
     * TagService
     */
    @Autowired
    private TagService tagService;

    /**
     * 标签云
     */
    @GetMapping
    public Result getTags() {
        return Result.success(tagService.list());
    }
}
