package com.zhashut.reggie.dto;

import com.zhashut.reggie.entity.Setmeal;
import com.zhashut.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    /**
     * 套餐菜品
     */
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
