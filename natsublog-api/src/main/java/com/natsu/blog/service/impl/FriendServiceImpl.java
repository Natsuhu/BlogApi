package com.natsu.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.enums.PageEnum;
import com.natsu.blog.mapper.FriendMapper;
import com.natsu.blog.model.dto.FriendDTO;
import com.natsu.blog.model.dto.FriendQueryDTO;
import com.natsu.blog.model.entity.Friend;
import com.natsu.blog.model.vo.SettingVO;
import com.natsu.blog.service.FriendService;
import com.natsu.blog.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Slf4j
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private SettingService settingService;

    @Override
    public void saveFriend(FriendDTO friendDTO) {
        //验证站点域名重复
        String website = friendDTO.getWebsite();
        try {
            URL url = new URL(website);
            website = url.getAuthority();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Friend::getId, Friend::getWebsite);
        queryWrapper.like(Friend::getWebsite, website);
        List<Friend> friends = friendMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(friends)) {
            log.warn("重复站点ID：{}", friends);
            throw new RuntimeException("此站点已存在");
        }
        //写入默认参数
        if (friendDTO.getIsPublished() == null) {
            friendDTO.setIsPublished(Constants.NO_PUBLISHED);
        }
        if (friendDTO.getClick() == null) {
            friendDTO.setClick(Constants.COM_NUM_ZERO);
        }
        //开始保存
        friendMapper.insert(friendDTO);
    }

    @Override
    public void updateFriend(FriendDTO friendDTO) {
        //验证站点域名重复
        String website = friendDTO.getWebsite();
        if (StrUtil.isNotBlank(website)) {
            try {
                URL url = new URL(website);
                website = url.getAuthority();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Friend::getId, Friend::getWebsite);
            queryWrapper.ne(Friend::getId, friendDTO.getId());
            queryWrapper.like(Friend::getWebsite, website);
            List<Friend> friends = friendMapper.selectList(queryWrapper);
            if (CollectionUtil.isNotEmpty(friends)) {
                log.warn("重复站点ID：{}", friends);
                throw new RuntimeException("此站点已存在");
            }
        }
        updateById(friendDTO);
    }

    @Override
    public void deleteFriend(FriendDTO friendDTO) {
        friendMapper.deleteById(friendDTO);
    }

    @Override
    public IPage<FriendDTO> getFriendTable(FriendQueryDTO friendQueryDTO) {
        IPage<FriendDTO> page = new Page<>(friendQueryDTO.getPageNo(), friendQueryDTO.getPageSize());
        return friendMapper.getFriendTable(page, friendQueryDTO);
    }

    @Override
    public SettingVO getFriendSetting() {
        return settingService.getPageSetting(PageEnum.FRIEND.getPageCode());
    }

    @Override
    public void updateFriendSetting(SettingVO settingVO) {
        if (settingVO.getFriendIsComment().equalsIgnoreCase("true") || settingVO.getFriendIsComment().equalsIgnoreCase("false")) {
            settingService.updateSetting(Constants.SETTING_KEY_FRIEND_IS_COMMENT, settingVO.getFriendIsComment());
        }
        settingService.updateSetting(Constants.SETTING_KEY_FRIEND_CONTENT, settingVO.getFriendContent());
    }

}
