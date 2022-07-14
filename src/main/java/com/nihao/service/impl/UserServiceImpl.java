package com.nihao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nihao.entity.User;
import com.nihao.mapper.UserMapper;
import com.nihao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {

}
