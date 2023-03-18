package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.entity.Employee;
import com.wtbu.mapper.EcployeeMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EcployeeMapper, Employee> implements EmployeeService{
}
