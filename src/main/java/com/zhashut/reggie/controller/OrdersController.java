package com.zhashut.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhashut.reggie.common.R;
import com.zhashut.reggie.entity.OrderDetail;
import com.zhashut.reggie.entity.Orders;
import com.zhashut.reggie.entity.ShoppingCart;
import com.zhashut.reggie.service.OrderDetailService;
import com.zhashut.reggie.service.OrdersService;
import com.zhashut.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 18:08
 * Description: No Description
 */
@SuppressWarnings({"all"})
@RequestMapping("/order")
@RestController
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 用户下单
     *
     * @param Orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 订单分页查询
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String number) {
        log.info("page: {}, pageSize: {}, number: {}", page, pageSize, number);
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //根据订单号查询
        queryWrapper.like(number != null, Orders::getNumber, number);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 派送订单
     */
    @PutMapping
    public R<String> delivery(@RequestBody Orders orders) {
        ordersService.updateById(orders);
        return R.success("订单派送成功");
    }

    /**
     * 移动端个人中心查询最新订单和历史订单功能
     */
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page, Integer pageSize) {
        log.info("page: {}, pageSize: {}", page, pageSize);

        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDetail> orderDetailPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 再来一单
     *
     * @return
     */
    @PostMapping("/again")
    public R<List<ShoppingCart>> orderAgain(@RequestBody Orders orders) {

        List<ShoppingCart> shoppingCarts = ordersService.orderAgainMethod(orders);
        shoppingCartService.saveBatch(shoppingCarts);

        return R.success(shoppingCarts);
    }

}
