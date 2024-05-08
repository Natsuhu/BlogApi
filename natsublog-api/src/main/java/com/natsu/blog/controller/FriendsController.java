package com.natsu.blog.controller;

import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.FriendDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.FriendService;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.service.async.AsyncTaskService;
import com.natsu.blog.utils.markdown.MarkdownUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * AsyncTaskService
     */
    @Autowired
    private AsyncTaskService asyncTaskService;

    /**
     * 获取friend列表
     */
    @VisitorLogger(VisitorBehavior.FRIEND)
    @GetMapping
    public Result getFriends() {
        List<FriendDTO> friendDTOS = friendService.getFriends();
        return Result.success(friendDTOS);
    }

    /**
     * 点击友链次数统计
     *
     * @param nickname 友链昵称
     * @return OK
     */
    @AccessLimit(seconds = 1, maxCount = 1)
    @VisitorLogger(VisitorBehavior.CLICK_FRIEND)
    @GetMapping("/clickFriend")
    public Result clickFriend(@RequestParam("nickname") String nickname) {
        asyncTaskService.updateFriendClickCount(friendService, nickname);
        return Result.success("OK");
    }

    /**
     * 获取友情链接页面配置
     */
    @GetMapping("/getSetting")
    public Result getFriendsPageSetting() {
        try {
            SettingVO settings = settingService.getPageSetting(PageEnum.FRIEND.getPageCode());
            //文案markdown转义
            settings.setFriendContent(MarkdownUtils.markdownToHtmlExtensions(settings.getFriendContent()));
            return Result.success(settings);
        } catch (Exception e) {
            log.error("获取友情链接页面配置失败：{}", e.getMessage());
            return Result.fail("获取友情链接页面配置失败：" + e.getMessage());
        }
    }
}
