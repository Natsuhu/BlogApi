package com.natsu.blog.model.vo;

import com.natsu.blog.model.entity.Category;
import com.natsu.blog.model.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReadArticle {

    private Integer id;

    private String title;

    private Date createTime;

    private Date updateTime;

    private Integer views;

    private Integer words;

    private Integer readTime;

    private Category category;

    private String content;

    private String authorName;

    private List<Tag> tags;

    private Boolean isCommentEnabled;

    private Boolean isAppreciation;
}
