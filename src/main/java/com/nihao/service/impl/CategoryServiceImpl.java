package com.nihao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nihao.common.CustomException;
import com.nihao.entity.Category;
import com.nihao.entity.Dish;
import com.nihao.entity.Setmeal;
import com.nihao.mapper.CategoryMapper;
import com.nihao.service.CategoryService;
import com.nihao.service.DishService;
import com.nihao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>implements CategoryService {
    /*
    根据id查询分类
     */
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
//查出删除菜品是否关联套餐
        LambdaQueryWrapper<Dish> dishqueryWrapper =new LambdaQueryWrapper<>();
        dishqueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishqueryWrapper);
        if (count1>0){
            //已经关联菜品，抛出异常
            throw new CustomException("当前分类关联其他菜品无法删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            //已经关联套餐，抛出异常
            throw new CustomException("当前分类关联其他套餐无法删除");
        }
        super.removeById(id);
    }
}
