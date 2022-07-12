package com.nihao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nihao.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
