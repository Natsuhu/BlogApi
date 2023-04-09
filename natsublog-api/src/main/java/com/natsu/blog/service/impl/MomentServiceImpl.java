package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.MomentMapper;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    @Autowired
    private MomentMapper momentMapper;

    public IPage<MomentDTO> getMoments(MomentQueryDTO queryCond) {
        IPage<MomentDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return momentMapper.getMoments(page, queryCond);
    }

}
