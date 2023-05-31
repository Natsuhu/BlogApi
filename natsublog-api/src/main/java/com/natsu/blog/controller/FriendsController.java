package com.natsu.blog.controller;

import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.FriendService;
import com.natsu.blog.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 博客前台，友情链接页面接口
 *
 * @author NatsuKaze
 */
@RestController
@RequestMapping("/friends")
public class FriendsController {

    /**
     * SiteSettingService
     */
    @Autowired
    private SiteSettingService siteSettingService;

    /**
     * FriendService
     */
    @Autowired
    private FriendService friendService;

    /**
     * 获取friend列表
     */
    @VisitorLogger(VisitorBehavior.FRIEND)
    @GetMapping
    public Result getFriends() {
//        List<FriendVO> friendVOS = friendService.getFriends();
//        return Result.success(friendVOS);
        return null;
    }

    /**
     * 获取友情链接页面配置
     */
    @GetMapping("/getSetting")
    public Result getFriendsPageSetting() {
        Map<String, String> settings = siteSettingService.getPageSetting(PageEnum.FRIEND.getPageCode());
        return Result.success(settings);
    }
}
