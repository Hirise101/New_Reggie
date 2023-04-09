package com.jyq.reggie.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyq.reggie.entity.Category;
import com.jyq.reggie.mapper.CategoryMapper;
import com.jyq.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
