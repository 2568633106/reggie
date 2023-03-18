package com.wtbu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtbu.entity.Category;
import com.wtbu.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
