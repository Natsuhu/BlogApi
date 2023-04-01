package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.VisitRecordMapper;
import com.natsu.blog.model.entity.VisitRecord;
import com.natsu.blog.service.VisitRecordService;
import org.springframework.stereotype.Service;

@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord> implements VisitRecordService {

}
