package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/setting")
public class AdminSettingController {

    @Autowired
    private SettingService settingService;

    @PostMapping("/getCommonSetting")
    public Result getCommonSetting() {
        try {
            SettingVO settingVO = new SettingVO();
            settingVO.setBlogName(settingService.getSetting(Constants.SETTING_KEY_BLOG_NAME));
            settingVO.setWebTitleSuffix(settingService.getSetting(Constants.SETTING_KEY_WEB_TITLE_SUFFIX));
            return Result.success(settingVO);
        } catch (Exception e) {
            log.error("获取通用设置失败：{}", e.getMessage());
            return Result.fail("获取通用设置失败：" + e.getMessage());
        }
    }

    @PostMapping("/updateCommonSetting")
    public Result updateCommonSetting(@RequestBody SettingVO settingVO) {
        try {
            if (StrUtil.isBlank(settingVO.getBlogName())) {
                return Result.fail("博客名称不能为空");
            }
            if (StrUtil.isBlank(settingVO.getWebTitleSuffix())) {
                return Result.fail("网页标题后缀不能为空");
            }
            settingService.updateSetting(Constants.SETTING_KEY_BLOG_NAME, settingVO.getBlogName());
            settingService.updateSetting(Constants.SETTING_KEY_WEB_TITLE_SUFFIX, settingVO.getWebTitleSuffix());
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新通用配置失败：{}", e.getMessage());
            return Result.fail("更新通用配置失败：" + e.getMessage());
        }
    }

    @PostMapping("/getCardSetting")
    public Result getCardSetting() {
        try {
            SettingVO settingVO = new SettingVO();
            settingVO.setCardAvatar(settingService.getSetting(Constants.SETTING_KEY_CARD_AVATAR));
            settingVO.setCardName(settingService.getSetting(Constants.SETTING_KEY_CARD_NAME));
            settingVO.setCardSignature(settingService.getSetting(Constants.SETTING_KEY_CARD_SIGNATURE));
            return Result.success(settingVO);
        } catch (Exception e) {
            log.error("获取资料卡设置失败：{}", e.getMessage());
            return Result.fail("获取资料卡设置失败：" + e.getMessage());
        }
    }

    @PostMapping("/updateCardSetting")
    public Result updateCardSetting(@RequestBody SettingVO settingVO) {
        try {
            if (StrUtil.isBlank(settingVO.getCardAvatar())) {
                return Result.fail("头像URL不能为空");
            }
            if (StrUtil.isBlank(settingVO.getCardName())) {
                return Result.fail("资料卡名称不能为空");
            }
            settingService.updateSetting(Constants.SETTING_KEY_CARD_AVATAR, settingVO.getCardAvatar());
            settingService.updateSetting(Constants.SETTING_KEY_CARD_NAME, settingVO.getCardName());
            settingService.updateSetting(Constants.SETTING_KEY_CARD_SIGNATURE, settingVO.getCardSignature());
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新资料卡配置失败：{}", e.getMessage());
            return Result.fail("更新资料卡配置失败：" + e.getMessage());
        }
    }

}
