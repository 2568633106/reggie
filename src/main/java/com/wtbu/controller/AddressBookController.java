package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wtbu.common.R;
import com.wtbu.entity.AddressBook;
import com.wtbu.entity.User;
import com.wtbu.service.AddressBookService;
import com.wtbu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public R<List<AddressBook>> getList(){
        return R.success(addressBookService.list());
    }

    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getPhone,addressBook.getPhone());
        User user = userService.getOne(queryWrapper);
        Long userId = user.getId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("新增地址成功");
    }

    @PutMapping("/default")
    public R<String> updateDefault(@RequestBody User user){
        Long id = user.getId();

        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getIsDefault,1);
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);

        AddressBook addressBook = addressBookService.getById(id);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }


    @GetMapping("/default")
    public R<AddressBook> getAddress(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        System.out.println(4);
        return R.success(addressBook);
    }


}
