package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.Moment;

import java.util.List;

public interface MomentMapper extends BaseMapper<Moment> {

    List<Moment> getPublicMoments();

}
