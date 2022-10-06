package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.pojo.Moment;

import java.util.List;

public interface MomentService extends IService<Moment> {

    List<Moment> getPublicMoments();

}
