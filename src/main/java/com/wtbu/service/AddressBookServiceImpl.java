package com.wtbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.entity.AddressBook;
import com.wtbu.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
