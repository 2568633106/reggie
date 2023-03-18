package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wtbu.common.R;
import com.wtbu.entity.Category;

public interface CategoryService extends IService<Category> {
    public R<String> CategoryremoveById(Long id);
}
