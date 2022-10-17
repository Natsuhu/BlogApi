package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.TagMapper;
import com.natsu.blog.model.entity.Tag;
import com.natsu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper , Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getTagsByArticleId(Integer articleId) {
        return tagMapper.getTagsByArticleId(articleId);
    }
}
