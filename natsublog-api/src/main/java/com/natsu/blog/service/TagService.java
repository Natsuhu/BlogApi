package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.TagDTO;
import com.natsu.blog.model.dto.TagQueryDTO;
import com.natsu.blog.model.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    /**
     * 根据文章ID获取对应标签
     */
    List<Tag> getTagsByArticleId(Long articleId);

    /**
     * 保存标签
     */
    void saveTag(TagDTO TagDTO);

    /**
     * 更新标签
     */
    void updateTag(TagDTO TagDTO);

    /**
     * 删除标签
     */
    void deleteTag(TagDTO TagDTO);

    /**
     * 获取标签表格
     */
    IPage<TagDTO> getTagTable(TagQueryDTO queryCond);

}
