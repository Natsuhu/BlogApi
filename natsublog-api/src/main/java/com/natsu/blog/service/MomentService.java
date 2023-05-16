package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.entity.Moment;

public interface MomentService extends IService<Moment> {

    /**
     * 前台。获取动态列表
     *
     * @param queryCond 查询条件
     * @return IPage<MomentDTO>
     */
    IPage<MomentDTO> getMoments(MomentQueryDTO queryCond);

    /**
     * 后台。获取将被更新的动态
     *
     * @param momentId 动态ID
     * @return MomentDTO
     */
    MomentDTO getUpdateMoment(Long momentId);

    /**
     * 后台。保存动态
     *
     * @param momentDTO momentDTO
     */
    void saveMoment(MomentDTO momentDTO);

    /**
     * 后台。更新动态
     *
     * @param momentDTO momentDTO
     */
    void updateMoment(MomentDTO momentDTO);

    /**
     * 后台。删除动态（物理）
     *
     * @param momentDTO momentDTO
     */
    void deleteMoment(MomentDTO momentDTO);

    /**
     * 后台。获取动态表格
     *
     * @param queryDTO 查询条件
     * @return IPage<MomentDTO>
     */
    IPage<MomentDTO> getMomentTable(MomentQueryDTO queryDTO);

}
