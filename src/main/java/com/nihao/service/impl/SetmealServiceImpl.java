package com.nihao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nihao.common.CustomException;
import com.nihao.dto.SetmealDto;
import com.nihao.entity.Setmeal;
import com.nihao.entity.SetmealDish;
import com.nihao.mapper.SetmealMapper;
import com.nihao.service.SetmealDishService;
import com.nihao.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count>0){
            //如果不能删除，则抛出异常
            throw new CustomException("套餐正在售卖中，不能删除 ");
        }
        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
