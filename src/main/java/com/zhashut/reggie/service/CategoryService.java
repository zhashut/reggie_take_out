package com.zhashut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhashut.reggie.entity.Category;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/20
 * Time: 11:21
 * Description: No Description
 */
@SuppressWarnings({"all"})
public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
