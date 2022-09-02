package com.natsu.blog.controller;

import com.natsu.blog.model.vo.Result;
import com.natsu.blog.pojo.Moment;
import com.natsu.blog.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/moments")
public class MomentController {

    @Autowired
    private MomentService momentService;

    @GetMapping
    public Result getPublicMoments() {
        List<Moment> momentList = momentService.getPublicMoments();
        return Result.success(momentList);
    }

}
