package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.VisitorDTO;
import com.natsu.blog.model.dto.VisitorQueryDTO;
import com.natsu.blog.model.entity.Visitor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorMapper extends BaseMapper<Visitor> {

    List<Visitor> getYesterdayVisitorInfo();

    IPage<VisitorDTO> getVisitorTable(IPage<VisitorDTO> page, @Param("queryCond") VisitorQueryDTO queryCond);

}
