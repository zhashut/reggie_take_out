package com.zhashut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhashut.reggie.dto.DishDto;
import com.zhashut.reggie.dto.SetmealDto;
import com.zhashut.reggie.entity.Setmeal;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/20
 * Time: 14:21
 * Description: No Description
 */
@SuppressWarnings({"all"})
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐信息和对应的菜品信息
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 更新套餐信息，同时更新对应的菜品信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);

    /**
     * 批量修改售卖菜品状态
     * @param status
     * @param ids
     */
    public void updateStatusByIds(Integer status, List<Long> ids);

}
