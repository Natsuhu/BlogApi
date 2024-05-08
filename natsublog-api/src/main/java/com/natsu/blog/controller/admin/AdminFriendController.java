package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.Admin;
import com.natsu.blog.model.dto.FriendDTO;
import com.natsu.blog.model.dto.FriendQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/friend")
public class AdminFriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/getFriendTable")
    public Result getFriendTable(@RequestBody FriendQueryDTO friendQueryDTO) {
        try {
            IPage<FriendDTO> result = friendService.getFriendTable(friendQueryDTO);
            return Result.success(result.getPages(), result.getTotal(), result.getRecords());
        } catch (Exception e) {
            log.error("获取友链表格失败，{}", e.getMessage());
            return Result.fail("获取友链表格失败：" + e.getMessage());
        }
    }

    @Admin
    @PostMapping("/saveFriend")
    public Result saveFriend(@RequestBody FriendDTO friendDTO) {
        if (friendDTO.getId() != null) {
            return Result.fail("不要携带ID");
        }
        Result result = checkParam(friendDTO);
        if (result != null) {
            return result;
        }
        try {
            friendService.saveFriend(friendDTO);
            return Result.success("保存成功");
        } catch (Exception e) {
            log.error("新增友链失败：{}", e.getMessage());
            return Result.fail("新增友链失败：" + e.getMessage());
        }
    }

    @Admin
    @PostMapping("/updateFriend")
    public Result updateFriend(@RequestBody FriendDTO friendDTO) {
        if (friendDTO.getId() == null) {
            return Result.fail("ID必填");
        }
        try {
            friendService.updateFriend(friendDTO);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新友链失败：{}", e.getMessage());
            return Result.fail("更新友链失败：" + e.getMessage());
        }
    }

    @Admin
    @PostMapping("/deleteFriend")
    public Result deleteFriend(@RequestBody FriendDTO friendDTO) {
        if (friendDTO.getId() == null) {
            return Result.fail("ID必填");
        }
        try {
            friendService.deleteFriend(friendDTO);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除友链失败：{}" + e.getMessage());
            return Result.fail("删除友链失败：" + e.getMessage());
        }
    }

    @PostMapping("/getFriendSetting")
    public Result getFriendSetting() {
        try {
            SettingVO friendSetting = friendService.getFriendSetting();
            return Result.success(friendSetting);
        } catch (Exception e) {
            log.error("获取友链配置失败：{}", e.getMessage());
            return Result.fail("获取友链配置失败：" + e.getMessage());
        }
    }

    @Admin
    @PostMapping("/updateFriendSetting")
    public Result updateFriendSetting(@RequestBody SettingVO settingVO) {
        try {
            friendService.updateFriendSetting(settingVO);
            return Result.success("更新友链配置成功");
        } catch (Exception e) {
            log.error("更新友链配置成功：{}", e.getMessage());
            return Result.fail("更新友链配置失败：" + e.getMessage());
        }
    }

    private Result checkParam(FriendDTO friendDTO) {
        if (StrUtil.isBlank(friendDTO.getNickname())) {
            return Result.fail("友链昵称必填");
        }
        if (StrUtil.isBlank(friendDTO.getDescription())) {
            return Result.fail("友链描述必填");
        }
        if (StrUtil.isBlank(friendDTO.getWebsite())) {
            return Result.fail("友链网址必填");
        }
        if (StrUtil.isBlank(friendDTO.getAvatar())) {
            return Result.fail("友链头像地址必填");
        }
        return null;
    }


}
