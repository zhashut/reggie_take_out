package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.entity.User;
import com.zhashut.reggie.mapper.UserMapper;
import com.zhashut.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 9:15
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
