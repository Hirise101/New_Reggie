package com.jyq.reggie.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyq.reggie.entity.Employee;
import com.jyq.reggie.mapper.EmployeeMapper;
import com.jyq.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
