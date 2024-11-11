package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.mapper.ArticleMapper;
import com.qihui.sun.model.Article;
import com.qihui.sun.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {
}
