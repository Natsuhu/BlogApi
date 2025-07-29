package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.MusicDTO;
import com.natsu.blog.model.dto.MusicQueryDTO;
import com.natsu.blog.model.entity.Music;

import java.util.List;

public interface MusicService extends IService<Music> {

    IPage<MusicDTO> getMusicTable(MusicQueryDTO queryCond);

    List<Music> getCatalogMusicList(String catalogId);

    void saveMusic(MusicDTO musicDTO);

    void updateMusic(MusicDTO musicDTO);

    void deleteMusic(MusicDTO musicDTO);

}
