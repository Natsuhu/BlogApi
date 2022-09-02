package com.natsu.blog.service;

import com.natsu.blog.pojo.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getTagList();

    Tag getTagById(int tagId);
}
