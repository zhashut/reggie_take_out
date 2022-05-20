package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.common.CustomException;
import com.zhashut.reggie.dto.DishDto;
import com.zhashut.reggie.entity.Dish;
import com.zhashut.reggie.entity.DishFlavor;
import com.zhashut.reggie.mapper.DishMapper;
import com.zhashut.reggie.service.DishFlavorService;
import com.zhashut.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/20
 * Time: 14:18
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品。同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Override
    @Transactional //添加事务操作
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        //菜品Id
        Long dishDtoId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors); //批量添加
    }

    /**
     * 根据id菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，从dish_falvor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 修改菜品。同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish基本信息
        this.updateById(dishDto);

        //清理当前菜品对应的口味数据--dish-flavorb表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品信息，同时删除对应的口味信息
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {

        //查询套餐状态，确认是否可以删除
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //根据id查询
        wrapper.in(Dish::getId, ids);
        //只有停售状态才能删,(1是启售，0是停售)
        wrapper.eq(Dish::getStatus, 1);

        long count = this.count(wrapper);

        if (count > 0) {
            //说明要删除的菜品中有启售中的,抛出异常
            throw new CustomException("菜品正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据--dish
        this.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        //根据id查询对应的口味数据
        queryWrapper.in(DishFlavor::getDishId, ids);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishFlavorService.removeByIds(dishFlavors);
    }

    /**
     * 批量修改售卖菜品状态
     * @param status
     * @param ids
     */
    @Override
    public void updateStatusByIds(Integer status, List<Long> ids) {
        List<Dish> dishList = this.listByIds(ids);
        dishList.stream().peek((dish) -> {
            dish.setStatus(status);
        }).collect(Collectors.toList());

        this.updateBatchById(dishList);
    }

}
