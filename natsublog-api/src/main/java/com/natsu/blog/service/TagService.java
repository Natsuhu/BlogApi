package com.natsu.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.model.dto.TagDTO;
import com.natsu.blog.model.dto.TagQueryDTO;
import com.natsu.blog.model.entity.Tag;

import java.util.List;

/**
 * 标签服务接口层
 *
 * @author NatsuKaze
 */
public interface TagService extends IService<Tag> {

    /**
     * 根据文章ID获取对应标签
     */
    List<Tag> getTagsByArticleId(Long articleId);

    /**
     * 保存标签
     */
    void saveTag(TagDTO tagDTO);

    /**
     * 更新标签
     */
    void updateTag(TagDTO tagDTO);

    /**
     * 删除标签
     */
    void deleteTag(TagDTO tagDTO);

    /**
     * 获取标签表格
     */
    IPage<TagDTO> getTagTable(TagQueryDTO queryCond);

}
