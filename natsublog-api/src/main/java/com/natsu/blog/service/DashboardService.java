package com.natsu.blog.service;

import com.natsu.blog.model.vo.DashboardVO;

import java.util.List;

public interface DashboardService {

    Integer getTodayVisitCount();

    Integer getArticleCount();

    Integer getCommentCount();

    List<DashboardVO> getCategoryEcharts();

    List<DashboardVO> getTagEcharts();

    List<DashboardVO> getMapEcharts();

}
