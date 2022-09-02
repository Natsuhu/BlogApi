package com.natsu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.natsu.blog.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getTagsByArticleId(int articleId);

    List<Tag> getTagList();

    Tag getTagById(int tagId);
}
