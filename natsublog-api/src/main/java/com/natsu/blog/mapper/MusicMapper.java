package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.MusicDTO;
import com.natsu.blog.model.dto.MusicQueryDTO;
import com.natsu.blog.model.entity.Music;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicMapper extends BaseMapper<Music> {

    IPage<MusicDTO> getMusicTable(IPage<MusicDTO> page, @Param("queryCond") MusicQueryDTO queryCond);

}
