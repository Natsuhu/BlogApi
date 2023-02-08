package com.natsu.blog.model.vo;

import com.natsu.blog.model.entity.Category;
import com.natsu.blog.model.entity.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HomeArticles {

    private Integer id;

    private String title;

    private Date createTime;

    private Integer views;

    private Integer words;

    private Integer readTime;

    private Boolean isTop;

    private Category category;

    private String description;

    private String thumbnail;

    private List<Tag> tags;

}
