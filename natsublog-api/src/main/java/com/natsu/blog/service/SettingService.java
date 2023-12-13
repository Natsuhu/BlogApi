package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.Setting;
import com.natsu.blog.model.vo.SettingVO;

public interface SettingService extends IService<Setting> {

    SettingVO getAllSetting();

    void updateSetting(String settingKey, String settingValue);

    String getSetting(String settingKey);

    SettingVO getPageSetting(Integer page);

}
