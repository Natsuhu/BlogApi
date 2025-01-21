package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.VisitorDTO;
import com.natsu.blog.model.dto.VisitorQueryDTO;
import com.natsu.blog.model.entity.Visitor;

import java.util.List;

public interface VisitorService extends IService<Visitor> {

    void saveVisitor(Visitor visitor);

    List<Visitor> getYesterdayVisitorInfo();

    IPage<VisitorDTO> getVisitorTable(VisitorQueryDTO queryCond);

}
