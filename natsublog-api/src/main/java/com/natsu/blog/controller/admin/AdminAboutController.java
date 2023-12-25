package com.natsu.blog.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/about")
public class AdminAboutController {

    @Autowired
    private SettingService settingService;

    @PostMapping("/getAboutSetting")
    public Result getAboutSetting() {
        try {
            SettingVO pageSetting = settingService.getPageSetting(PageEnum.ABOUT.getPageCode());
            return Result.success(pageSetting);
        } catch (Exception e) {
            log.error("获取关于我配置失败：{}", e.getMessage());
            return Result.fail("获取关于我配置失败：" + e.getMessage());
        }
    }

    @PostMapping("/updateAboutSetting")
    public Result updateAboutSetting(@RequestBody SettingVO settingVO) {
        try {
            //验证必填项非空
            if (StrUtil.isBlank(settingVO.getAboutContent())) {
                return Result.fail("关于我内容不能为空");
            }
            if (StrUtil.isBlank(settingVO.getAboutTitle())) {
                return Result.fail("关于我标题不能为空");
            }
            //更新配置
            if (settingVO.getAboutIsComment().equalsIgnoreCase("true") || settingVO.getAboutIsComment().equalsIgnoreCase("false")) {
                settingService.updateSetting(Constants.SETTING_KEY_ABOUT_IS_COMMENT, settingVO.getAboutIsComment());
            }
            settingService.updateSetting(Constants.SETTING_KEY_ABOUT_CONTENT, settingVO.getAboutContent());
            settingService.updateSetting(Constants.SETTING_KEY_ABOUT_TITLE, settingVO.getAboutTitle());
            settingService.updateSetting(Constants.SETTING_KEY_ABOUT_MUSIC_SERVER, settingVO.getAboutMusicServer());
            settingService.updateSetting(Constants.SETTING_KEY_ABOUT_MUSIC_ID, settingVO.getAboutMusicId());
            return Result.success("更新关于我配置成功");
        } catch (Exception e) {
            log.error("更新关于我配置失败：{}", e.getMessage());
            return Result.fail("更新关于我配置失败：" + e.getMessage());
        }
    }

}
