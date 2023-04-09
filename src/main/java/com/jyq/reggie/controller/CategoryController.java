package com.jyq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyq.reggie.common.R;
import com.jyq.reggie.entity.Category;
import com.jyq.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageInfo(int page,int pageSize,String name){
        log.info("执行分页查询:page={},pageSize={},name={}",page,pageSize,name);
        //分页构造器
        Page pageInfo=new Page(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.orderByAsc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 新增分类，由于新增菜品分类和套餐分类在同一页面，传输数据时只有type不同，所以统一由addCategory来执行
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        log.info("新增分类：{}",category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改菜品分类：{}",category.toString());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 删除分类信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@PathVariable Long ids){
        log.info("删除分类信息");
        categoryService.removeById(ids);
        return R.success("删除成功");
    }
}
