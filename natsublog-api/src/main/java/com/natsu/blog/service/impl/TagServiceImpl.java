package com.natsu.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.constant.Constants;
import com.natsu.blog.mapper.TagMapper;
import com.natsu.blog.model.dto.TagDTO;
import com.natsu.blog.model.dto.TagQueryDTO;
import com.natsu.blog.model.entity.ArticleTag;
import com.natsu.blog.model.entity.Tag;
import com.natsu.blog.service.ArticleTagService;
import com.natsu.blog.service.TagService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public List<Tag> getTagsByArticleId(Long articleId) {
        return tagMapper.getTagsByArticleId(articleId);
    }

    @Override
    public void saveTag(TagDTO tagDTO) {
        //校验名称重复
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tagDTO.getName().trim());
        if (!ObjectUtils.isEmpty(tagMapper.selectOne(queryWrapper))) {
            throw new RuntimeException("此标签已存在");
        }
        //组装实体
        //TODO 需要验证十六进制颜色格式正确性
        if (StringUtils.isBlank(tagDTO.getColor())) {
            tagDTO.setColor(Constants.TAG_DEFAULT_COLOR);
        }
        tagDTO.setCreateTime(new Date());
        tagDTO.setUpdateTime(new Date());
        //开始保存
        tagMapper.insert(tagDTO);
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        //校验名称重复
        if (StrUtil.isNotBlank(tagDTO.getName())) {
            LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ne(Tag::getId, tagDTO.getId());
            queryWrapper.eq(Tag::getName, tagDTO.getName().trim());
            if (CollectionUtil.isNotEmpty(tagMapper.selectList(queryWrapper))) {
                throw new RuntimeException("此标签已存在");
            }
        } else {
            tagDTO.setName(null);
        }
        //组装实体
        //TODO 需要验证十六进制颜色格式正确性
        tagDTO.setUpdateTime(new Date());
        //开始更新
        tagMapper.updateById(tagDTO);
    }

    @Override
    public void deleteTag(TagDTO tagDTO) {
        //验证该标签下有文章
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getTagId, tagDTO.getId());
        if (articleTagService.count(queryWrapper) > 0) {
            throw new RuntimeException("该标签下存在文章");
        }
        //删除标签
        tagMapper.deleteById(tagDTO);
    }

    @Override
    public IPage<TagDTO> getTagTable(TagQueryDTO queryCond) {
        IPage<TagDTO> page = new Page<>(queryCond.getPageNo(), queryCond.getPageSize());
        return tagMapper.getTagTable(page, queryCond);
    }


}
