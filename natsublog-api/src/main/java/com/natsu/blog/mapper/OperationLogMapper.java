package com.natsu.blog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.OperationLogDTO;
import com.natsu.blog.model.dto.OperationLogQueryDTO;
import com.natsu.blog.model.entity.OperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    IPage<OperationLogDTO> getOperationLogTable(IPage<OperationLogDTO> page, @Param("queryCond") OperationLogQueryDTO queryCond);

}
