package com.qihui.sun.controller;

import com.alibaba.fastjson2.JSONArray;
import com.qihui.sun.model.Category;
import com.qihui.sun.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {


    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public JSONArray selectAllCategoryList(){
        List<Category> list = categoryService.list();
        return JSONArray.from(list);
    }

}
