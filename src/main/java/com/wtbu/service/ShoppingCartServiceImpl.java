package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.entity.ShoppingCart;
import com.wtbu.entity.User;
import com.wtbu.mapper.ShoppingCartMapper;
import com.wtbu.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
