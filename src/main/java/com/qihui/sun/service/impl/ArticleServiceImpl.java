package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.mapper.ArticleMapper;
import com.qihui.sun.model.Article;
import com.qihui.sun.service.ArticleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public boolean saveArticle(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setDeleteFlag(0);
        return save(article);
    }
}