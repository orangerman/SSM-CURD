package com.javafan.crud.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javafan.crud.bean.Employee;
import com.javafan.crud.bean.Msg;
import com.javafan.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 处理员工的CRUD请求
 */
@Controller
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;


    /**
     * @param pn
     * @return
     * @ResponseBody 正常工作 需要导入JackSon包
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {

        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();
        PageInfo page = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo",page);


    }


    /**
     * 查询员工数据
     * 分页查询
     * 不是用json 安卓和移动端适配不
     * @return
     */
//   @RequestMapping("/emps")
//    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
//        //这不是一个分页查询
//        //引入PageHelper分页插件
//        //从第几页开始查 以及分页每页的大小
//        PageHelper.startPage(pn, 5);
//        //startPage紧跟的这个查询就是一个分页查询
//        List emps = employeeService.getAll();
//        //使用PageInfo包装查询后的结果 只需要将pageInfo交给页面
//        //封装了详细的分页信息  包括有我们查询出来的数据//navigatePage连续显示的页数 5
//        PageInfo page = new PageInfo(emps,5);
//        model.addAttribute("pageInfo", page);
//        return "list";
//    }


}
