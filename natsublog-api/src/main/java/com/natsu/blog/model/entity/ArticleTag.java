package com.natsu.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description
 * @Author NatsuKaze
 * @Date 2022-10-06
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@TableName("article_tag")
public class ArticleTag implements Serializable {

    /**
     * 文章ID
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 标签ID
     */
    @TableField("tag_id")
    private Long tagId;

}
