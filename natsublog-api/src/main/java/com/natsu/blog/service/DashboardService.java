package com.natsu.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.natsu.blog.model.vo.DashboardVO;

import java.util.List;

public interface DashboardService {

    Integer getTodayVisitCount();

    Integer getArticleCount();

    Integer getCommentCount();

    List<DashboardVO> getCategoryEcharts();

    List<DashboardVO> getTagEcharts();

    List<DashboardVO> getMapEcharts();

    JSONObject getLineEcharts();

}
