package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.SiteSetting;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteSettingMapper extends BaseMapper<SiteSetting> {

    List<SiteSetting> getPageSetting(int page);

}
