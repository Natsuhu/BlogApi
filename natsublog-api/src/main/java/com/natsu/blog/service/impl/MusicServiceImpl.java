package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.MusicMapper;
import com.natsu.blog.model.dto.MusicDTO;
import com.natsu.blog.model.dto.MusicQueryDTO;
import com.natsu.blog.model.entity.Music;
import com.natsu.blog.model.entity.MusicCatalog;
import com.natsu.blog.service.AnnexService;
import com.natsu.blog.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private AnnexService annexService;

    @Override
    public IPage<MusicDTO> getMusicTable(MusicQueryDTO queryCond) {
        IPage<MusicDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        IPage<MusicDTO> musicTable = musicMapper.getMusicTable(page, queryCond);
        List<MusicDTO> records = musicTable.getRecords();
        //封面换为URL
        for (MusicDTO musicDTO : records) {
            musicDTO.setPicViewUrl(annexService.getAnnexAccessAddress(musicDTO.getPicUrl()));
        }
        IPage<MusicDTO> pageResult = new Page<>(musicTable.getCurrent(), musicTable.getSize(), musicTable.getTotal());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public List<Music> getCatalogMusicList(String catalogId) {
        LambdaQueryWrapper<Music> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Music::getCatalogId, catalogId);
        queryWrapper.eq(Music::getIsPublished, true);
        return list(queryWrapper);
    }

    @Override
    public void saveMusic(MusicDTO musicDTO) {
        if (musicDTO.getIsPublished() == null) {
            musicDTO.setIsPublished(true);
        }
        musicMapper.insert(musicDTO);
    }

    @Override
    public void updateMusic(MusicDTO musicDTO) {
        //更新时，如果URL变动，要删除附件记录。暂时先不动
        updateById(musicDTO);
    }

    @Override
    public void deleteMusic(MusicDTO musicDTO) {
        //删除时要一并删除文件。暂时先只删除记录
        musicMapper.deleteById(musicDTO.getId());
    }

}
