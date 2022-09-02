package com.natsu.blog.service.impl;

import com.natsu.blog.mapper.TagMapper;
import com.natsu.blog.pojo.Tag;
import com.natsu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    public List<Tag> getTagList() {
        return tagMapper.getTagList();
    }

    public Tag getTagById(int tagId) {
        return tagMapper.getTagById(tagId);
    }

}
