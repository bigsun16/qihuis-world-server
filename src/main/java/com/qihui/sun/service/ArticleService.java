package com.qihui.sun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qihui.sun.domain.Article;
import com.qihui.sun.domain.PageReq;

/**
 * @author bigsu
 * @description 针对表【article】的数据库操作Service
 * @createDate 2024-12-24 17:24:42
 */
public interface ArticleService extends IService<Article> {
    boolean saveArticle(Article article);
    boolean deleteById(Integer id);

    Page<Article> pageQuery(PageReq pageReq);
}
