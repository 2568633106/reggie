package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.entity.OrderDetail;
import com.wtbu.mapper.OrderDetailMapper;
import com.wtbu.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
