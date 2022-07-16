package com.nihao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nihao.entity.ShoppingCart;
import com.nihao.mapper.ShoppingCartMapper;
import com.nihao.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>implements ShoppingCartService {
}
