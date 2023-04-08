package com.jyq.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyq.reggie.common.R;
import com.jyq.reggie.entity.Employee;
import com.jyq.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    //注入
    @Autowired
    private EmployeeService employeeService;

    /**
     * 执行分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //控制台输出日志
        log.info("执行分页查询:page={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件过滤器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //添加过滤条件,这里使用like作为模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
     public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        log.info("登录账户");
        //获得密码并进行md5加密
        String password= employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据username获得employee对象
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //判断用户能否登录
        if(emp==null){
            return R.error("用户不存在，登陆失败");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误，登陆失败");
        }
        if(emp.getStatus()==0){
            return R.error("账号已禁用，登陆失败");
        }
        //在session中设置属性为employee，值为id
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
     }

    /**
     * 登出
     * @param request
     * @return
     */
     @PostMapping("/logout")
     public R<String> logout(HttpServletRequest request){
        log.info("账户退出");
        //清理session中的属性值
         request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
     }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工：employee={}",employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //将密码进行加密
        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateTime(LocalDateTime.now());
        //设置公共字段
        employeeService.save(employee);
        return R.success("新增用户成功");
    }

    /**
     * 根据员工id查询员工信息并回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息....");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
    }

    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long emId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(emId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("信息修改成功");
    }




}
