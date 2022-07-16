package com.nihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nihao.common.R;
import com.nihao.dto.SetmealDto;
import com.nihao.entity.Category;
import com.nihao.entity.Dish;
import com.nihao.entity.Setmeal;
import com.nihao.entity.SetmealDish;
import com.nihao.mapper.SetmealMapper;
import com.nihao.service.CategoryService;
import com.nihao.service.SetmealDishService;
import com.nihao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }
    /*
        删除操作
         */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }
    /*
    启售
     */
    @PostMapping("/status/1")
    public R<String> update(@RequestParam List<Long> ids) {
        for (Long id : ids) {
        UpdateWrapper<Setmeal> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        Setmeal setmeal=new Setmeal();
        setmeal.setStatus(1);
        setmealService.update(setmeal,updateWrapper);}
        return R.success("成功");
    }
    @PostMapping("status/0")
    public R<String> update1(@RequestParam List<Long> ids){
        for (Long id : ids) {
            UpdateWrapper<Setmeal> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            Setmeal setmeal=new Setmeal();
            setmeal.setStatus(0);
            setmealService.update(setmeal,updateWrapper);
        }return R.success("成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(lambdaQueryWrapper);
        return R.success(setmealList);
    }
    /*
    查看套餐
     */
    @GetMapping("dish/{id}")
    public R<List<SetmealDto>> get(@PathVariable Long id){
//        log.info("{id}",id);
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId,setmeal.getId());
        List<Setmeal> setmealList= setmealService.list(queryWrapper);
        List<SetmealDto> setmealDtoList=setmealList.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
           Long categoryId = item.getCategoryId();
            SetmealDish setmealDish = setmealDishService.getById(categoryId);
            if (setmealDish!=null){
                String setmealDishName = setmealDish.getName();
                setmealDto.setCategoryName(setmealDishName);
            }
//           SetmealDish setmealDish = setmealDishService.getById(categoryId);
         LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
         lambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
           List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
//           BeanUtils.copyProperties(list,setmealList);
            setmealDto.setSetmealDishes(list);
           return setmealDto;
       }).collect(Collectors.toList());
        return R.success(setmealDtoList);
    }

}

