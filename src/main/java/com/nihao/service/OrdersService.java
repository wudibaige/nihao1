package com.nihao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nihao.entity.Orders;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
