package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.MusicCatalogMapper;
import com.natsu.blog.model.dto.MusicCatalogDTO;
import com.natsu.blog.model.dto.MusicCatalogQueryDTO;
import com.natsu.blog.model.entity.MusicCatalog;
import com.natsu.blog.service.MusicCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicCatalogServiceImpl extends ServiceImpl<MusicCatalogMapper, MusicCatalog> implements MusicCatalogService {

    @Autowired
    private MusicCatalogMapper musicCatalogMapper;

    @Override
    public List<MusicCatalogDTO> getMusicCatalogList(MusicCatalogQueryDTO queryCond) {
        return musicCatalogMapper.getMusicCatalogList(queryCond);
    }

    @Override
    public void saveMusicCatalog(MusicCatalogDTO musicCatalogDTO) {
        if (musicCatalogDTO.getIsPublished() == null) {
            musicCatalogDTO.setIsPublished(true);
        }
        musicCatalogMapper.insert(musicCatalogDTO);
    }

    @Override
    public void updateMusicCatalog(MusicCatalogDTO musicCatalogDTO) {
        updateById(musicCatalogDTO);
    }

}
