package com.natsu.blog.controller;

import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.FriendService;
import com.natsu.blog.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客前台，友情链接页面接口
 *
 * @author NatsuKaze
 */
@Slf4j
@RestController
@RequestMapping("/friends")
public class FriendsController {

    /**
     * SettingService
     */
    @Autowired
    private SettingService settingService;

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
        try {
            SettingVO settings = settingService.getPageSetting(PageEnum.FRIEND.getPageCode());
            return Result.success(settings);
        } catch (Exception e) {
            log.error("获取友情链接页面配置失败：{}", e.getMessage());
            return Result.fail("获取友情链接页面配置失败：" + e.getMessage());
        }
    }
}
