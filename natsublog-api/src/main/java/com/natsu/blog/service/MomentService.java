package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Moment;

public interface MomentService extends IService<Moment> {

    IPage<Moment> getPublicMoments(BaseQueryDTO baseQueryDTO);

}
