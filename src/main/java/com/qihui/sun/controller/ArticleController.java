package com.qihui.sun.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.qihui.sun.controller.response.ApiResponse;
import com.qihui.sun.domain.Article;
import com.qihui.sun.permission.RateLimit;
import com.qihui.sun.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@SaCheckLogin
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @SaIgnore
    @GetMapping("/list")
    @RateLimit(permitsPerSecond = 100)
    public ApiResponse<List<Article>> selectAllArticleList(@RequestParam String categoryKey) {
        List<Article> categoryList = articleService.query().eq("category_key", categoryKey).eq("delete_flag", 0).list();
        return ApiResponse.success(categoryList);
    }

    @RateLimit(permitsPerSecond = 80)
    @PostMapping("/addOrUpdate")
    public ApiResponse<Boolean> addArticle(@RequestBody Article article) {
        boolean b = articleService.saveArticle(article);
        return ApiResponse.success(b);
    }

    @DeleteMapping("/{id}")
    @RateLimit(permitsPerSecond = 100)
    public ApiResponse<Boolean> deleteArticle(@PathVariable Integer id) {
        boolean b = articleService.deleteById(id);
        return ApiResponse.success(b);
    }
}
