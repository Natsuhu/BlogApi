package com.natsu.blog.mapper;

import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.vo.DashboardVO;
import org.apache.ibatis.annotations.Param;
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

    List<DashboardVO> getLineEcharts(@Param("queryCond") BaseQueryDTO queryCond);
}
