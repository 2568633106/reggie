package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.entity.User;
import com.wtbu.mapper.UserMapper;
import com.wtbu.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
