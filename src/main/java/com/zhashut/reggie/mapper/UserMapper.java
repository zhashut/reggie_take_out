package com.zhashut.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhashut.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/23
 * Time: 9:14
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
