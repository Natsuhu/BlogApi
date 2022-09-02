package com.natsu.blog.service.impl;

import com.natsu.blog.mapper.MomentMapper;
import com.natsu.blog.pojo.Moment;
import com.natsu.blog.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MomentServiceImpl implements MomentService {

    @Autowired
    private MomentMapper momentMapper;

    public List<Moment> getPublicMoments() {
        return momentMapper.getPublicMoments();
    }

}
