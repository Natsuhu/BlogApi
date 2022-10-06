package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.Moment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MomentMapper extends BaseMapper<Moment> {

    List<Moment> getPublicMoments();

}
