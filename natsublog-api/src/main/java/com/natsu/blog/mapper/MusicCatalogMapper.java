package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.model.dto.MusicCatalogDTO;
import com.natsu.blog.model.dto.MusicCatalogQueryDTO;
import com.natsu.blog.model.entity.MusicCatalog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicCatalogMapper extends BaseMapper<MusicCatalog> {

    /**
     * 获取歌单列表
     *
     * @param queryCond 查询条件
     * @return 歌单列表
     */
    List<MusicCatalogDTO> getMusicCatalogList(@Param("queryCond") MusicCatalogQueryDTO queryCond);

}
