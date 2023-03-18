package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wtbu.entity.Dish;
import com.wtbu.entity.DishDto;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
}
