package com.natsu.blog.mapper;

import com.natsu.blog.model.vo.DashboardVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardMapper {

    Integer getTodayVisitCount();

    Integer getArticleCount();

    Integer getCommentCount();

    List<DashboardVO> getCategoryEcharts();

    List<DashboardVO> getTagEcharts();

    List<DashboardVO> getMapEcharts();
}
