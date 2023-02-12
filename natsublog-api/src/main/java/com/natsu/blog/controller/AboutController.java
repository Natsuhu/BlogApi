package com.natsu.blog.controller;

import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 博客前台，关于我页面前端接口
 *
 * @author NatsuKaze
 * */
@RestController
@RequestMapping("/about")
public class AboutController {

    /**
     * SiteSettingService
     * */
    @Autowired
    private SiteSettingService siteSettingService;

    /**
     * 获取关于我页面配置
     * */
    @VisitorLogger(VisitorBehavior.ABOUT)
    @GetMapping("getSetting")
    public Result getAboutPageSetting() {
        Map<String , String> settings = siteSettingService.getPageSetting(Constants.PAGE_SETTING_ABOUT);
        return Result.success(settings);
    }

}
