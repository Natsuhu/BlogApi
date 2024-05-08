package com.natsu.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.annotation.AccessLimit;
import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.dto.Result;
import com.natsu.blog.service.MomentService;
import com.natsu.blog.service.async.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客前端，动态页面接口
 *
 * @author NatsuKaze
 */
@RestController
@RequestMapping("/moments")
public class MomentController {

    /**
     * MomentService
     */
    @Autowired
    private MomentService momentService;

    /**
     * AsyncTaskService
     */
    @Autowired
    private AsyncTaskService asyncTaskService;

    /**
     * 获取动态
     */
    @VisitorLogger(VisitorBehavior.MOMENT)
    @GetMapping
    public Result getPublicMoments(MomentQueryDTO queryCond) {
        IPage<MomentDTO> moments = momentService.getMoments(queryCond);
        return Result.success(moments.getPages(), moments.getTotal(), moments.getRecords());
    }

    /**
     * 点赞动态
     * @param id id
     * @return result
     */
    @AccessLimit(seconds = 86400, maxCount = 1, msg = "请勿重复点赞哦")
    @VisitorLogger(VisitorBehavior.LIKE_MOMENT)
    @GetMapping("/like/{id}")
    public Result like(@PathVariable String id) {
        asyncTaskService.momentLike(momentService, id);
        return Result.success("OK");
    }

}
