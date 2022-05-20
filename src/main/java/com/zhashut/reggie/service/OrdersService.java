package com.zhashut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhashut.reggie.entity.Orders;
import com.zhashut.reggie.entity.ShoppingCart;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 18:06
 * Description: No Description
 */
@SuppressWarnings({"all"})
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param Orders
     */
    public void submit(Orders orders);

    /**
     * 再来一单
     */
    public List<ShoppingCart> orderAgainMethod(Orders orders);

}
