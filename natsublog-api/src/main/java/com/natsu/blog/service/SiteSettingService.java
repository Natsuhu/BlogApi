package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.Setting;

import java.util.Map;

public interface SiteSettingService extends IService<Setting> {
    Map<String, String> getPageSetting(Integer page);
}
