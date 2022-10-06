package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.pojo.SiteSetting;

import java.util.Map;

public interface SiteSettingService extends IService<SiteSetting> {
    Map getPageSetting(int page);
}
