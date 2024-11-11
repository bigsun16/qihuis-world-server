package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.mapper.CategoryMapper;
import com.qihui.sun.model.Category;
import com.qihui.sun.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
