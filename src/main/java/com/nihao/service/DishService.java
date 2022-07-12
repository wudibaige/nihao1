package com.nihao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nihao.common.R;
import com.nihao.dto.DishDto;
import com.nihao.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品
    public void saveWithFlavor(DishDto dishDto);
    //  根据id查询对应的菜品信息和口味信息
    public DishDto getByIdWithFlavor(long id);
    //更新菜品信息、口味信息
    public void updateWithFlavor(DishDto dishDto);
    //修改状态

}
