package com.natsu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.natsu.blog.pojo.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<Tag> getTagList();

    Tag getTagById(int tagId);
}
