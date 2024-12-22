package com.qihui.sun.controller;

import com.qihui.sun.controller.response.ApiResponse;
import com.qihui.sun.domain.Category;
import com.qihui.sun.service.CategoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/list")
    public ApiResponse<List<Category>> selectAllCategoryList() {
        List<Category> list = categoryService.list();
        return ApiResponse.success(list);
    }

}
