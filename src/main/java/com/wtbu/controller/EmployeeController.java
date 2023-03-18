package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtbu.common.BaseContext;
import com.wtbu.common.CommostConst;
import com.wtbu.common.R;
import com.wtbu.entity.Employee;
import com.wtbu.mapper.EcployeeMapper;
import com.wtbu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);


        if (emp==null) {
            R.error(CommostConst.LOGIN_FAIL);
        }
        if (!emp.getPassword().equals(password)) {
            R.error(CommostConst.LOGIN_FAIL);
        }
        if (emp.getStatus()==CommostConst.EMPLOYEE_STATUS_NO) {
            R.error(CommostConst.LOGIN_ACCOUNT_STOP);
        }
        request.getSession().setAttribute("id",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("id");
        return R.success("退出成功");
    }

    @PostMapping
    public  R<String> addUser(HttpServletRequest request,@RequestBody Employee employee){
        String password = "123456";
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        Long id = (Long) request.getSession().getAttribute("id");
        employee.setPassword(password);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page> getPageInfo(int page, int pageSize,String name){
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> updateStatus(HttpServletRequest request,@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> show(@PathVariable Long id){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Employee::getId,id);
        Employee emp = employeeService.getOne(queryWrapper);
        return R.success(emp);
    }

}
