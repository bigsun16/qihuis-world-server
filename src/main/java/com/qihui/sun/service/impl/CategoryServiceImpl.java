package com.qihui.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihui.sun.domain.Category;
import com.qihui.sun.service.CategoryService;
import com.qihui.sun.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * @author bigsu
 * @description 针对表【category】的数据库操作Service实现
 * @createDate 2024-12-24 17:24:42
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

}




