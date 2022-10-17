package com.natsu.blog.controller;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    private SiteSettingService siteSettingService;

    @GetMapping
    public Result getFriendsPageSetting() {
        Map<String , String> settings = siteSettingService.getPageSetting(3);
        return Result.success(settings);
    }

}
