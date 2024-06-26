package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.FriendDTO;
import com.natsu.blog.model.dto.FriendQueryDTO;
import com.natsu.blog.model.entity.Friend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMapper extends BaseMapper<Friend> {

    List<FriendDTO> getFriends();

    IPage<FriendDTO> getFriendTable(IPage<FriendDTO> page, @Param("queryCond") FriendQueryDTO queryCond);

}
