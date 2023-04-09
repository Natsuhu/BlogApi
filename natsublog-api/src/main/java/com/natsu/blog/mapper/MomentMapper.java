package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.entity.Moment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MomentMapper extends BaseMapper<Moment> {

    IPage<MomentDTO> getMoments(IPage<MomentDTO> page, @Param("queryCond") MomentQueryDTO queryCond);

}
