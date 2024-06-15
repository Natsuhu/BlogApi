package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.VisitLogDTO;
import com.natsu.blog.model.dto.VisitLogQueryDTO;
import com.natsu.blog.model.entity.VisitLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogMapper extends BaseMapper<VisitLog> {

    IPage<VisitLogDTO> getVisitLogTable(IPage<VisitLogDTO> page, @Param("queryCond") VisitLogQueryDTO queryCond);

}
