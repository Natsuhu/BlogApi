package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.natsu.blog.model.dto.TagDTO;
import com.natsu.blog.model.dto.TagQueryDTO;
import com.natsu.blog.model.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getTagsByArticleId(Long articleId);

    IPage<TagDTO> getTagTable(IPage<TagDTO> page, @Param("queryCond") TagQueryDTO queryCond);
}
