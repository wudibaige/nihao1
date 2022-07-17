package com.nihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nihao.common.BaseContext;
import com.nihao.common.R;
import com.nihao.entity.ShoppingCart;
import com.nihao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null){
        queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one!=null){
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now()) ;
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }
        return R.success(one);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("成功");
    }
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaUpdateWrapper<ShoppingCart> updateWrapper=new LambdaUpdateWrapper<>();
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        updateWrapper.eq(dishId!=null,ShoppingCart::getDishId,dishId);
        updateWrapper.eq(setmealId!=null,ShoppingCart::getSetmealId,setmealId);
        ShoppingCart one = shoppingCartService.getOne(updateWrapper);
//        ShoppingCart one = shoppingCartService.getById(dishId);
        Integer number = one.getNumber();
        if (dishId!=null){
        updateWrapper.eq(ShoppingCart::getDishId,dishId);
        updateWrapper.ge(ShoppingCart::getNumber,1);
//        updateWrapper.set(ShoppingCart::getNumber,number-1);
        shoppingCart.setNumber(number-1);
//        shoppingCartService.updateById(shoppingCart);
        shoppingCartService.update(shoppingCart,updateWrapper);
        }else {
            updateWrapper.eq(ShoppingCart::getSetmealId,setmealId);
            updateWrapper.gt(ShoppingCart::getNumber,1);
            shoppingCart.setNumber(number-1);
            shoppingCartService.update(shoppingCart,updateWrapper);
        }
//        shoppingCartService.updateById(shoppingCart);
        return R.success("成功");
    }
}
