package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.mapper.ArticleMapper;
import com.qihui.sun.model.Article;
import com.qihui.sun.service.ArticleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private Logger logger = Logger.getLogger(ArticleServiceImpl.class.getName());
    @Override
    public boolean saveArticle(Article article) {
        Integer id = article.getId();
        if (id != null && getById(id) != null) {
            logger.info("Updating existing article");
            return updateById(article);
        } else {
            logger.info("Saving new article");
            article.setCreateTime(LocalDate.now());
            article.setUpdateTime(LocalDate.now());
            article.setDeleteFlag(0);
            return save(article);
        }
    }
}