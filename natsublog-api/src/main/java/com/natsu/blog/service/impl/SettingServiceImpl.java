package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.SettingMapper;
import com.natsu.blog.model.entity.Setting;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    @Override
    public SettingVO getAllSetting() {
        return getPageSetting(-1);
    }

    @Override
    public void updateSetting(String settingKey, String settingValue) {
        LambdaUpdateWrapper<Setting> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Setting::getSettingKey, settingKey);
        wrapper.set(Setting::getSettingValue, settingValue);
        update(wrapper);
    }

    @Override
    public String getSetting(String settingKey) {
        LambdaQueryWrapper<Setting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setting::getSettingKey, settingKey);
        return settingMapper.selectOne(wrapper).getSettingValue();
    }

    public SettingVO getPageSetting(Integer page) {
        LambdaQueryWrapper<Setting> wrapper = new LambdaQueryWrapper<>();
        if (page >= 0) {
            wrapper.eq(Setting::getPage, page);
        }
        //获取list转为map
        List<Setting> settings = settingMapper.selectList(wrapper);
        Map<String, String> allSettingMap = settings
                .stream()
                .collect(Collectors.toMap(Setting::getSettingKey, x -> x.getSettingValue() == null ? "" : x.getSettingValue()));
        //填充VO对象
        SettingVO settingVO = new SettingVO();
        settingVO.setBlogName(allSettingMap.get(Constants.SETTING_KEY_BLOG_NAME));
        settingVO.setWebTitleSuffix(allSettingMap.get(Constants.SETTING_KEY_WEB_TITLE_SUFFIX));
        settingVO.setFriendContent(allSettingMap.get(Constants.SETTING_KEY_FRIEND_CONTENT));
        settingVO.setFriendIsComment(allSettingMap.get(Constants.SETTING_KEY_FRIEND_IS_COMMENT));
        settingVO.setAboutIsComment(allSettingMap.get(Constants.SETTING_KEY_ABOUT_IS_COMMENT));
        settingVO.setAboutContent(allSettingMap.get(Constants.SETTING_KEY_ABOUT_CONTENT));
        settingVO.setAboutMusicId(allSettingMap.get(Constants.SETTING_KEY_ABOUT_MUSIC_ID));
        settingVO.setCardName(allSettingMap.get(Constants.SETTING_KEY_CARD_NAME));
        settingVO.setCardSignature(allSettingMap.get(Constants.SETTING_KEY_CARD_SIGNATURE));
        settingVO.setCardAvatar(allSettingMap.get(Constants.SETTING_KEY_CARD_AVATAR));
        return settingVO;
    }

}
