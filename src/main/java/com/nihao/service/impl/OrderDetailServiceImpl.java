package com.nihao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nihao.entity.OrderDetail;
import com.nihao.mapper.OrderDetailMapper;
import com.nihao.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {
}
