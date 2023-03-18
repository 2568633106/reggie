package com.wtbu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtbu.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
