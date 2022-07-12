package com.nihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nihao.common.R;
import com.nihao.entity.Category;
import com.nihao.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
    log.info("category:{}",category);
    categoryService.save(category);
    return R.success("新增菜品成功");
    }
    /*
    分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
    //分页构造器
    Page<Category> pageInfo=new Page<>(page,pageSize);
    LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Category::getSort);
    categoryService.page(pageInfo,queryWrapper);
    return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类{}",ids);
//        categoryService.removeById(id);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息{}",category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    /*
    根据条件查询分类
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list=categoryService.list(queryWrapper);
        return R.success(list);
    }

}
