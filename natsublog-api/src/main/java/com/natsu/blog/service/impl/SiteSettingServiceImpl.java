package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.SiteSettingMapper;
import com.natsu.blog.pojo.SiteSetting;
import com.natsu.blog.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiteSettingServiceImpl extends ServiceImpl<SiteSettingMapper , SiteSetting> implements SiteSettingService {

    @Autowired
    private SiteSettingMapper siteSettingMapper;

    public Map getPageSetting(int page) {
        List<SiteSetting> settings = siteSettingMapper.getPageSetting(page);
        Map<String,String> resultMap = new HashMap<>();
        for (SiteSetting siteSetting : settings) {
            resultMap.put(siteSetting.getNameEn(),siteSetting.getContent());
        }
        return resultMap;
    }

}
