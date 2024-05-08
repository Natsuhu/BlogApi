package com.natsu.blog.controller;

import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private SettingService settingService;

    /**
     * 获取首页配置项
     * @return settingVO
     */
    @GetMapping("/getSetting")
    public Result getIndexSetting() {
        try {
            SettingVO pageSetting = settingService.getPageSetting(PageEnum.INDEX.getPageCode());
            return Result.success(pageSetting);
        } catch (Exception e) {
            log.error("获取首页配置失败：{}", e.getMessage());
            return Result.fail("获取首页配置失败：" + e.getMessage());
        }
    }

}
