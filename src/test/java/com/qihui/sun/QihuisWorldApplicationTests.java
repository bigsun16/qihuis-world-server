package com.qihui.sun;

import com.qihui.sun.model.Article;
import com.qihui.sun.service.impl.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.time.LocalDateTime;

@SpringBootTest
class QihuisWorldApplicationTests {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Test
    public void test() {
        Article article = new Article();
        article.setTitle("深入理解java虚拟机");
        article.setContent("这是一本讲解javajvm的书籍");
        article.setCategoryKey("java");
        article.setUpdateTime(LocalDateTime.now());
        article.setCreateTime(null);
        articleService.save(article);
        articleService.findAll();
    }
    
    
    @Test
    public void test2(){
        Criteria criteria = new Criteria("title").is("读书");
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<Article> search = elasticsearchOperations.search(criteriaQuery, Article.class);
        search.get().forEach(articleSearchHit -> System.out.println(articleSearchHit.getContent()));
    }
    @Test
    public void test3(){
        Criteria criteria = new Criteria("categoryKey").is("sample");
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<Article> search = elasticsearchOperations.search(criteriaQuery, Article.class);
        search.get().forEach(articleSearchHit -> System.out.println(articleSearchHit.getContent()));
    }

}
