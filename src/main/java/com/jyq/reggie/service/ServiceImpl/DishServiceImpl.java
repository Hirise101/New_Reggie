package com.jyq.reggie.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyq.reggie.entity.Dish;
import com.jyq.reggie.mapper.DishMapper;
import com.jyq.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
