package com.qihui.sun.controller;

import com.alibaba.fastjson2.JSONArray;
import com.qihui.sun.model.Article;
import com.qihui.sun.service.impl.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public JSONArray selectAllCategoryList(@RequestParam String categoryKey){
        List<Article> categoryKey1 = articleService.searchByCategory(categoryKey);
        return JSONArray.from(categoryKey1);
    }
    @PostMapping("/add")
    public void addArticle(@RequestBody Article article){
        articleService.save(article);
    }

}
