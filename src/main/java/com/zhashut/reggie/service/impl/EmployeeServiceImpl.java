package com.zhashut.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhashut.reggie.entity.Employee;
import com.zhashut.reggie.mapper.EmployeeMapper;
import com.zhashut.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/18
 * Time: 14:56
 * Description: No Description
 */
@SuppressWarnings({"all"})
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
