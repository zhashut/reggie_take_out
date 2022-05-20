package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.common.BaseContext;
import com.zhashut.reggie.common.CustomException;
import com.zhashut.reggie.entity.*;
import com.zhashut.reggie.mapper.OrdersMapper;
import com.zhashut.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 18:07
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param Orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //根据当前用户id查询
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId(); //从订单中获取地址id
        AddressBook addressBook = addressBookService.getById(addressBookId); //获取地址信息
        if (addressBook == null) {
            throw new CustomException("地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId(); //订单号

        AtomicInteger amount = new AtomicInteger(0);

        //添加订单明细数据
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //添加订单数据
        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2); //设置订单状态
        orders.setAmount(new BigDecimal(amount.get())); //总金额
        orders.setUserId(userId);
//        orders.setUserName(user.getName()); //TODO 获取不到不知道为啥，先放着
        orders.setUserName(addressBook.getConsignee());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入一条数据
        this.save(orders);

        //向订单明细表插入多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }

    /**
     * 再来一单
     *
     * @param orders
     */
    @Override
    public List<ShoppingCart> orderAgainMethod(Orders orders) {
        //根据当前订单id
        Long ordersId = orders.getId();

        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        //根据订单Id查询
        queryWrapper.in(OrderDetail::getOrderId, ordersId);
        //获取订单里的菜品/套餐数据
        List<OrderDetail> list = orderDetailService.list(queryWrapper);

        List<ShoppingCart> shoppingCartList = list.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            Long currentId = BaseContext.getCurrentId();
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            BigDecimal amount = item.getAmount();
            Integer number = item.getNumber();
            String name = item.getName();
            String dishFlavor = item.getDishFlavor();
            String image = item.getImage();

            if (dishId != null) {
                shoppingCart.setDishId(dishId);
            } else {
                shoppingCart.setSetmealId(setmealId);
            }
            //将数据设置进购物车
            shoppingCart.setUserId(currentId);
            shoppingCart.setAmount(amount);
            shoppingCart.setNumber(number);
            shoppingCart.setName(name);
            shoppingCart.setDishFlavor(dishFlavor);
            shoppingCart.setImage(image);

            return shoppingCart;
        }).collect(Collectors.toList());

        return shoppingCartList;
    }


}
