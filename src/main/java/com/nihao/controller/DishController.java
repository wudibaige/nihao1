package com.nihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nihao.common.R;
import com.nihao.dto.DishDto;
import com.nihao.entity.Category;
import com.nihao.entity.Dish;
import com.nihao.entity.DishFlavor;
import com.nihao.service.CategoryService;
import com.nihao.service.DishFlavorService;
import com.nihao.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    /*
    新增菜品
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }
    /*
    菜品信息分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Dish> pageInfo=new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<Dish>();
        //添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (categoryId!=null){
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);}
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    /*
    根据id查询对应的菜品信息和口味信息
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
       dishService.updateWithFlavor(dishDto);
    return R.success("修改菜品成功");
    }
    /*
    删除
     */
    @DeleteMapping
    public R<String> delete(@RequestParam(value = "ids",defaultValue = "") String ids){
   List<String> delist=new ArrayList<String>();
        String[] split = ids.split(",");
        for (String s : split) {
            delist.add(s);
            dishService.removeByIds(delist);
        }
        return R.success("删除成功");
    }
    /*
    停售
     */
    @PostMapping("/status/0")
    public R<String> update(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            UpdateWrapper<Dish> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            Dish dish=new Dish();
            dish.setStatus(0);
            dishService.update(dish,updateWrapper);
        }
       return R.success("成功");
    }
    @PostMapping("/status/1")
    public R<String> update1(@RequestParam List<Long> ids){
        for (Long id : ids) {
            UpdateWrapper<Dish> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            Dish dish=new Dish();
            dish.setStatus(1);
            dishService.update(dish,updateWrapper);
        }
        return R.success("修改成功");
    }
    /*
    根据条件查询对应的套餐信息
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //构建查询条件
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList=list.stream().map((iter)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(iter,dishDto);
            Long categoryId = iter.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = iter.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}

