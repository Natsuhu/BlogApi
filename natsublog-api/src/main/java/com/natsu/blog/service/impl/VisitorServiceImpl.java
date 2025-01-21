package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.VisitorMapper;
import com.natsu.blog.model.dto.VisitorDTO;
import com.natsu.blog.model.dto.VisitorQueryDTO;
import com.natsu.blog.model.entity.Visitor;
import com.natsu.blog.service.VisitorService;
import com.natsu.blog.service.async.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Override
    public void saveVisitor(Visitor visitor) {
        asyncTaskService.saveVisitor(visitorMapper, visitor);
    }

    @Override
    public List<Visitor> getYesterdayVisitorInfo() {
        return visitorMapper.getYesterdayVisitorInfo();
    }

    @Override
    public IPage<VisitorDTO> getVisitorTable(VisitorQueryDTO queryCond) {
        IPage<VisitorDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return visitorMapper.getVisitorTable(page, queryCond);
    }

}
