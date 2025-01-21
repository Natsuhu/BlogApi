package com.natsu.blog.service.impl;

import com.natsu.blog.mapper.DashboardMapper;
import com.natsu.blog.model.vo.DashboardVO;
import com.natsu.blog.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Override
    public Integer getTodayVisitCount() {
        return dashboardMapper.getTodayVisitCount();
    }

    @Override
    public Integer getArticleCount() {
        return dashboardMapper.getArticleCount();
    }

    @Override
    public Integer getCommentCount() {
        return dashboardMapper.getCommentCount();
    }

    @Override
    public List<DashboardVO> getCategoryEcharts() {
        return dashboardMapper.getCategoryEcharts();
    }

    @Override
    public List<DashboardVO> getTagEcharts() {
        return dashboardMapper.getTagEcharts();
    }
}
