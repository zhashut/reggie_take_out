package com.zhashut.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhashut.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/18
 * Time: 14:54
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
