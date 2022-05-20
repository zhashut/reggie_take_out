package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.entity.ShoppingCart;
import com.zhashut.reggie.mapper.ShoppingCartMapper;
import com.zhashut.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 14:50
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
