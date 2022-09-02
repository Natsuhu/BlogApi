package com.natsu.blog.model.vo;

import com.natsu.blog.pojo.Category;
import com.natsu.blog.pojo.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HomeArticleList {

    private Integer id;

    private String title;

    private Date createTime;

    private Integer views;

    private Integer words;

    private Integer readTime;

    private Category category;

    private String description;

    private String thumbnail;

    private List<Tag> tags;


}
