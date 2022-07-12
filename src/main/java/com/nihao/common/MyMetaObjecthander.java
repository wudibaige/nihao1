package com.nihao.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
/*
自定义元对象处理器
 */
public class MyMetaObjecthander implements MetaObjectHandler {
    /*
    插入操作，自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
    metaObject.setValue("createTime", LocalDateTime.now());
    metaObject.setValue("updateTime",LocalDateTime.now());
    metaObject.setValue("createUser",BaseContext.getCurrentId() );
    metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
