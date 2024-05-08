package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.FriendDTO;
import com.natsu.blog.model.dto.FriendQueryDTO;
import com.natsu.blog.model.entity.Friend;
import com.natsu.blog.model.vo.SettingVO;

import java.util.List;

public interface FriendService extends IService<Friend> {

    List<FriendDTO> getFriends();

    void saveFriend(FriendDTO friendDTO);

    void updateFriend(FriendDTO friendDTO);

    void deleteFriend(FriendDTO friendDTO);

    IPage<FriendDTO> getFriendTable(FriendQueryDTO friendQueryDTO);

    SettingVO getFriendSetting();

    void updateFriendSetting(SettingVO settingVO);

}
