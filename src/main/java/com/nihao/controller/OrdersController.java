package com.nihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nihao.common.R;
import com.nihao.entity.Orders;
import com.nihao.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getOrderTime);
        ordersService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @GetMapping("userPage")
    public R<Page> page1(int page,int pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}
