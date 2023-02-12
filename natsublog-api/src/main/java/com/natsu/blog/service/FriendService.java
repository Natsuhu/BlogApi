package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.entity.Friend;
import com.natsu.blog.model.vo.FriendVO;

import java.util.List;

public interface FriendService extends IService<Friend> {

    List<FriendVO> getFriends();

}
