package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.VisitorMapper;
import com.natsu.blog.model.entity.Visitor;
import com.natsu.blog.service.VisitorService;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {
}
