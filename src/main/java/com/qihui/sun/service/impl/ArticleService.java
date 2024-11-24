package com.qihui.sun.service.impl;

import com.qihui.sun.model.Article;
import com.qihui.sun.service.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public void findAll() {
        articleRepository.findAll().forEach(System.out::println);
    }

    public void save(Article article) {
        article.setCreateTime(LocalDate.now());
        article.setUpdateTime(LocalDate.now());
        article.setDeleteFlag(false);
        articleRepository.save(article);
    }

    public List<Article> searchByCategory(String categoryKey) {

        Criteria criteria = new Criteria("categoryKey").is(categoryKey);
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<Article> search = elasticsearchOperations.search(criteriaQuery, Article.class);
        return search.get().map(SearchHit::getContent).toList();
    }
}
