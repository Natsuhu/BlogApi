package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.MomentMapper;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper , Moment> implements MomentService {

    @Autowired
    private MomentMapper momentMapper;

    public PageResult<Moment> getPublicMoments(BaseQueryDTO baseQueryDTO) {
        IPage<Moment> page = new Page<>(baseQueryDTO.getPageNo() , baseQueryDTO.getPageSize());
        LambdaQueryWrapper<Moment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Moment::getIsPublished , 1);
        wrapper.orderByDesc(Moment::getCreateTime);
        IPage<Moment> moments = momentMapper.selectPage(page , wrapper);
        return new PageResult<>(moments.getPages() , moments.getRecords());
    }

}
