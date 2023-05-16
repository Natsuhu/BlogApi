package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.MomentMapper;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.service.MomentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    @Autowired
    private MomentMapper momentMapper;

    public IPage<MomentDTO> getMoments(MomentQueryDTO queryCond) {
        IPage<MomentDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return momentMapper.getMoments(page, queryCond);
    }

    @Override
    public MomentDTO getUpdateMoment(Long momentId) {
        Moment moment = momentMapper.selectById(momentId);
        MomentDTO momentDTO = new MomentDTO();
        BeanUtils.copyProperties(moment, momentDTO);
        return momentDTO;
    }

    @Override
    public void saveMoment(MomentDTO momentDTO) {
        //组装实体
        momentDTO.setId(null);
        if (momentDTO.getLikes() == null) {
            momentDTO.setLikes(Constants.COM_NUM_ZERO.longValue());
        }
        //TODO 1.应该新增字段“发布时间”。2.不应该叫author,字段名改为nickName比较合适。3.头像和名称前端未设置时，应该从数据库的setting表读取
        if (momentDTO.getCreateTime() == null) {
            momentDTO.setCreateTime(new Date());
        }
        if (StringUtils.isBlank(momentDTO.getAvatar())) {
            momentDTO.setAvatar("/avatar.jpg");
        }
        if (StringUtils.isBlank(momentDTO.getAuthor())) {
            momentDTO.setAuthor(Constants.DEFAULT_AUTHOR);
        }
        momentDTO.setUpdateTime(new Date());
        //保存动态
        momentMapper.insert(momentDTO);
    }

    @Override
    public void updateMoment(MomentDTO momentDTO) {
        momentDTO.setUpdateTime(new Date());
        momentMapper.updateById(momentDTO);
    }

    @Override
    public void deleteMoment(MomentDTO momentDTO) {
        //TODO 需要删除对应评论
        momentMapper.deleteById(momentDTO);
    }

    @Override
    public IPage<MomentDTO> getMomentTable(MomentQueryDTO queryDTO) {
        IPage<MomentDTO> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        return momentMapper.getMomentTable(page, queryDTO);
    }

}
