package com.natsu.blog.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.natsu.blog.mapper.DashboardMapper;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.vo.DashboardVO;
import com.natsu.blog.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<DashboardVO> getMapEcharts() {
        return dashboardMapper.getMapEcharts();
    }

    @Override
    public JSONObject getLineEcharts() {
        //请求参数，最近30天
        BaseQueryDTO baseQueryDTO = new BaseQueryDTO();
        DateTime endTime = DateUtil.date();
        DateTime startTime = DateUtil.offsetDay(endTime, -10);
        baseQueryDTO.setStartTime(startTime);
        baseQueryDTO.setEndTime(endTime);
        //组装响应
        List<DashboardVO> lineData = dashboardMapper.getLineEcharts(baseQueryDTO);
        List<String> dateList = lineData.stream().map(DashboardVO::getDate).collect(Collectors.toList());
        List<Integer> uvList = lineData.stream().map(DashboardVO::getUv).collect(Collectors.toList());
        List<Integer> pvList = lineData.stream().map(DashboardVO::getPv).collect(Collectors.toList());
        //组装响应
        JSONObject lineEcharts = new JSONObject();
        lineEcharts.put("dateList", dateList);
        lineEcharts.put("uvList", uvList);
        lineEcharts.put("pvList", pvList);
        return lineEcharts;
    }
}
