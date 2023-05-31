package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.SettingMapper;
import com.natsu.blog.model.entity.Setting;
import com.natsu.blog.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SiteSettingService {

    @Autowired
    private SettingMapper settingMapper;

    public Map<String, String> getPageSetting(Integer page) {
        LambdaQueryWrapper<Setting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setting::getPage, page);
        List<Setting> settings = settingMapper.selectList(wrapper);
        Map<String, String> resultMap = new HashMap<>(settings.size());
        for (Setting setting : settings) {
            resultMap.put(setting.getKey(), setting.getValue());
        }
        return resultMap;
    }

}
