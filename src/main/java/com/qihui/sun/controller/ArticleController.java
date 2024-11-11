package com.qihui.sun.controller;

import com.alibaba.fastjson2.JSONArray;
import com.qihui.sun.model.Article;
import com.qihui.sun.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {
    @Autowired
    private ArticleService articleService;

//    public void setArticleService(ArticleService articleService) {
//        this.articleService = articleService;
//    }

    @GetMapping("/list")
    public JSONArray selectAllCategoryList(@RequestParam String categoryKey){
//        List<Article> list = articleService.list();
        List<Article> categoryKey1 = articleService.listByMap(Collections.singletonMap("category_key", categoryKey));
        return JSONArray.from(categoryKey1);
    }


}
