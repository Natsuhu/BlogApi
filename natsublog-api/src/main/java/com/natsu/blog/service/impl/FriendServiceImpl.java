package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.FriendMapper;
import com.natsu.blog.model.entity.Friend;
import com.natsu.blog.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendMapper friendMapper;

//    @Override
//    public List<FriendVO> getFriends() {
//        //查询
//        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Friend::getIsPublished, Constants.PUBLISHED);
//        queryWrapper.eq(Friend::getAudit, Constants.AUDIT);
//        List<Friend> friends = friendMapper.selectList(queryWrapper);
//        //封装
//        List<FriendVO> friendVOS = new ArrayList<>(friends.size());
//        for (Friend friend : friends) {
//            FriendVO friendVO = new FriendVO();
//            BeanUtils.copyProperties(friend, friendVO);
//            friendVOS.add(friendVO);
//        }
//        return friendVOS;
//    }
}
