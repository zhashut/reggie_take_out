package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.entity.OrderDetail;
import com.zhashut.reggie.mapper.OrderDetailMapper;
import com.zhashut.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 18:10
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
