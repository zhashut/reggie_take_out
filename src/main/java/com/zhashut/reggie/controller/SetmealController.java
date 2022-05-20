package com.zhashut.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhashut.reggie.common.R;
import com.zhashut.reggie.dto.SetmealDto;
import com.zhashut.reggie.entity.Category;
import com.zhashut.reggie.entity.Setmeal;
import com.zhashut.reggie.service.CategoryService;
import com.zhashut.reggie.service.SetmealDishService;
import com.zhashut.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/20
 * Time: 14:23
 * Description: No Description
 */
@SuppressWarnings({"all"})
/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true) //清除缓存数据
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息： {}", setmealDto.toString());
        setmealService.saveWithDish(setmealDto);

        //清理所有菜品的缓存数据
//        Set keys = redisTemplate.keys("setmeal_*");
//        redisTemplate.delete(keys);

        //清理某个分类下面的菜品缓存数据
    /*    String key = "dish_" + setmealDto.getCategoryId() + "_" + setmealDto.getStatus();
        redisTemplate.delete(key);*/

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page<Setmeal> pageinfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加模糊查询条件
        queryWrapper.like(name != null, Setmeal::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //查询
        setmealService.page(pageinfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageinfo, dtoPage, "records");

        List<Setmeal> records = pageinfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true) //清除缓存数据
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 批量修改套餐状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatusByIds(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("修改状态");
        setmealService.updateStatusByIds(status, ids);
        return R.success("售卖状态修改成功");
    }

    /**
     * 根据套餐查询条件
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {

        /*List<Setmeal> list = null;

        //动态构造key
        String key = "setmeal_" + setmeal.getCategoryId() + "_" + setmeal.getStatus();
        //先从Redis中获取缓存数据
        list = (List<Setmeal>) redisTemplate.opsForValue().get(key);

        if (list != null) {
            //如果存在，直接返回，无需查询数据库
            return R.success(list);
        }*/

        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //查询对应的分页id
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        //查询对应的状态信息
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis,并设置时间为60分钟
//        redisTemplate.opsForValue().set(key, list, 60, TimeUnit.MINUTES);

        return R.success(list);
    }

    /**
     * 根据套餐id查询套餐信息和对应的菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    @CacheEvict(value = "setmealCache", allEntries = true) //方式三：使用注解清除缓存数据
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);

        //方式一：清理所有菜品的缓存数据
//        Set keys = redisTemplate.keys("setmeal_*");
//        redisTemplate.delete(keys);

        //方式二 ：清理某个分类下面的菜品缓存数据
     /*   String key = "setmeal_" + setmealDto.getCategoryId() + "_" + setmealDto.getStatus();
        redisTemplate.delete(key);*/

        return R.success("修改套餐成功");
    }
}
