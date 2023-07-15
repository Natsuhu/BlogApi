package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.ArticleTagMapper;
import com.natsu.blog.model.entity.ArticleTag;
import com.natsu.blog.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
