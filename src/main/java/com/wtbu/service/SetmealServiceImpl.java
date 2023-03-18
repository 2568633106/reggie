package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.entity.Setmeal;
import com.wtbu.mapper.SetmealMapper;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
}
