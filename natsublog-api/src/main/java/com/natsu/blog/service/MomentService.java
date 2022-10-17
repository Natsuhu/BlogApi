package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.BaseQueryDTO;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.model.vo.PageResult;

public interface MomentService extends IService<Moment> {

    PageResult<Moment> getPublicMoments(BaseQueryDTO baseQueryDTO);

}
