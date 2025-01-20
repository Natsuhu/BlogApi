package com.natsu.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.MomentMapper;
import com.natsu.blog.model.dto.MomentDTO;
import com.natsu.blog.model.dto.MomentQueryDTO;
import com.natsu.blog.model.entity.Moment;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.MomentService;
import com.natsu.blog.service.SettingService;
import com.natsu.blog.utils.CommonUtils;
import com.natsu.blog.utils.markdown.MarkdownUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    @Autowired
    private MomentMapper momentMapper;

    @Autowired
    private AnnexService annexService;

    @Autowired
    private SettingService settingService;

    @Override
    public IPage<MomentDTO> getMoments(MomentQueryDTO queryCond) {
        IPage<MomentDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        IPage<MomentDTO> moments = momentMapper.getMoments(page, queryCond);
        //Markdown转义HTML，设置头像地址
        List<MomentDTO> records = moments.getRecords();
        for (MomentDTO momentDTO : records) {
            momentDTO.setContent(MarkdownUtils.markdownToHtmlExtensions(momentDTO.getContent()));
            momentDTO.setAvatar(annexService.getAnnexAccessAddress(momentDTO.getAvatar()));
        }
        moments.setRecords(records);
        return moments;
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
        if (momentDTO.getPublishTime() == null) {
            momentDTO.setPublishTime(new Date());
            momentDTO.setEditTime(new Date());
        } else {
            momentDTO.setEditTime(momentDTO.getPublishTime());
        }
        if (StringUtils.isBlank(momentDTO.getAuthor())) {
            momentDTO.setAuthor(Constants.DEFAULT_AUTHOR);
        }
        //设置头像为当前资料卡头像
        momentDTO.setAvatar(settingService.getSetting(Constants.SETTING_KEY_CARD_AVATAR));
        //保存动态
        momentMapper.insert(momentDTO);
    }

    @Override
    public void updateMoment(MomentDTO momentDTO) {
        //验证动态内容改变
        Moment dbMoment = momentMapper.selectById(momentDTO);
        if (!CommonUtils.compareString(dbMoment.getContent(), momentDTO.getContent())) {
            momentDTO.setEditTime(new Date());
        }
        //开始更新
        momentMapper.updateById(momentDTO);
    }

    @Override
    public void deleteMoment(MomentDTO momentDTO) {
        momentMapper.deleteById(momentDTO);
    }

    @Override
    public IPage<MomentDTO> getMomentTable(MomentQueryDTO queryDTO) {
        IPage<MomentDTO> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        IPage<MomentDTO> momentTable = momentMapper.getMomentTable(page, queryDTO);
        List<MomentDTO> records = momentTable.getRecords();
        for (MomentDTO momentDTO : momentTable.getRecords()) {
            momentDTO.setAvatar(annexService.getAnnexAccessAddress(momentDTO.getAvatar()));
        }
        momentTable.setRecords(records);
        return momentTable;
    }

}
