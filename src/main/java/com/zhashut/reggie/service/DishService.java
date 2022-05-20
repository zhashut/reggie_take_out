package com.zhashut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhashut.reggie.dto.DishDto;
import com.zhashut.reggie.entity.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/20
 * Time: 14:18
 * Description: No Description
 */
@SuppressWarnings({"all"})
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);

    //删除菜品信息，同时删除对应的口味信息
    public void removeWithFlavor(List<Long> ids);

    //批量修改售卖菜品状态
    public void updateStatusByIds(Integer status, List<Long> ids);

}
