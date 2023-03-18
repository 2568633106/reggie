package com.wtbu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtbu.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcployeeMapper extends BaseMapper<Employee> {
}
