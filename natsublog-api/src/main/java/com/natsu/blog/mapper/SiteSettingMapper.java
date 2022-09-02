package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.SiteSetting;

import java.util.List;

public interface SiteSettingMapper extends BaseMapper<SiteSetting> {

    List<SiteSetting> getPageSetting(int page);

}
