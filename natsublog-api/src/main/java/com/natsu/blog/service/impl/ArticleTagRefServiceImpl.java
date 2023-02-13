package com.natsu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.natsu.blog.mapper.ArticleTagRefMapper;
import com.natsu.blog.model.entity.ArticleTagRef;
import com.natsu.blog.service.ArticleTagRefService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagRefServiceImpl extends ServiceImpl<ArticleTagRefMapper , ArticleTagRef> implements ArticleTagRefService {

}
