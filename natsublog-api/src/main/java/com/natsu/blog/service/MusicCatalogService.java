package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.MusicCatalogDTO;
import com.natsu.blog.model.dto.MusicCatalogQueryDTO;
import com.natsu.blog.model.entity.MusicCatalog;

import java.util.List;

public interface MusicCatalogService extends IService<MusicCatalog> {

    /**
     * 获取歌单列表
     *
     * @param queryCond 查询条件
     * @return 歌单列表
     */
    List<MusicCatalogDTO> getMusicCatalogList(MusicCatalogQueryDTO queryCond);

    void saveMusicCatalog(MusicCatalogDTO musicCatalogDTO);

    void updateMusicCatalog(MusicCatalogDTO musicCatalogDTO);

}
