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

@RestController
@Slf4j
@RequestMapping("/admin/setting")
public class AdminSettingController {

    @Autowired
    private SettingService settingService;

    @PostMapping("/getCommonSetting")
    public Result getCommonSetting() {
        try {
            SettingVO pageSetting = settingService.getPageSetting(PageEnum.INDEX.getPageCode());
            return Result.success(pageSetting);
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

}
