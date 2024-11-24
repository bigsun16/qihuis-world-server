package com.qihui.sun.service;

import com.qihui.sun.model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<Article,String> {
}
