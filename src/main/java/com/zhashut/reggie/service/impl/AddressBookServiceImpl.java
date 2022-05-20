package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.entity.AddressBook;
import com.zhashut.reggie.mapper.AddressBookMapper;
import com.zhashut.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 11:10
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
