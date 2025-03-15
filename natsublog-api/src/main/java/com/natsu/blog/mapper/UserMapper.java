package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.UserDTO;
import com.natsu.blog.model.dto.UserQueryDTO;
import com.natsu.blog.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

    IPage<UserDTO> getUserTable(IPage<UserDTO> page, @Param("queryCond") UserQueryDTO queryCond);

}
