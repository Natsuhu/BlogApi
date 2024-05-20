package com.natsu.blog.controller.admin;

import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/dashboard")
@Slf4j
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取顶部的4块基础数据
     *
     * @return result
     */
    @PostMapping("/getTopBaseData")
    public Result getTopBaseData() {
        try {
            Integer articleCount = dashboardService.getArticleCount();
            Integer commentCount = dashboardService.getCommentCount();
            HashMap<String, Integer> result = new HashMap<>();
            result.put("pv", 100);
            result.put("uv", 100);
            result.put("articleCount", articleCount);
            result.put("commentCount", commentCount);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取顶部数据失败：{}", e.getMessage());
            return Result.fail("获取顶部数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取分类饼图
     *
     * @return result
     */
    @PostMapping("/getCategoryEcharts")
    public Result getCategoryEcharts() {
        try {
            return Result.success(dashboardService.getCategoryEcharts());
        } catch (Exception e) {
            log.error("获取分类饼图数据失败：{}", e.getMessage());
            return Result.fail("获取分类饼图数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取标签饼图
     *
     * @return result
     */
    @PostMapping("/getTagEcharts")
    public Result getTagEcharts() {
        try {
            return Result.success(dashboardService.getTagEcharts());
        } catch (Exception e) {
            log.error("获取标签饼图数据失败：{}", e.getMessage());
            return Result.fail("获取标签饼图数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取地图数据
     *
     * @return result
     */
    @PostMapping("/getMapEcharts")
    public Result getMapEcharts() {
        return Result.success("OK");
    }

    /**
     * 获取底部访客数据折线图
     *
     * @return result
     */
    @PostMapping("/getVisitRecordEcharts")
    public Result getVisitRecordEcharts() {
        return Result.success("OK");
    }

}
