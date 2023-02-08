package com.natsu.blog.controller;

import com.natsu.blog.annotation.VisitorLogger;
import com.natsu.blog.enums.VisitorBehavior;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.model.vo.PageResult;
import com.natsu.blog.model.vo.Result;
import com.natsu.blog.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客前端，动态页面接口
 *
 * @author NatsuKaze
 * */
@RestController
@RequestMapping("/moments")
public class MomentController {

    /**
     * MomentService
     * */
    @Autowired
    private MomentService momentService;

    /**
     * 获取动态
     * */
    @VisitorLogger(VisitorBehavior.MOMENT)
    @GetMapping
    public Result getPublicMoments(BaseQueryDTO baseQueryDTO) {
        PageResult<Moment> moments = momentService.getPublicMoments(baseQueryDTO);
        return Result.success(moments);
    }

}
