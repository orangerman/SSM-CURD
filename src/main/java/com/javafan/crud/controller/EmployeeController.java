package com.javafan.crud.controller;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javafan.crud.bean.Employee;
import com.javafan.crud.bean.Msg;
import com.javafan.crud.service.EmployeeService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.nativejdbc.OracleJdbc4NativeJdbcExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工的CRUD请求
 */
@Controller
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;




//    /**
//     * 删除单条记录
//     * @param empId
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.DELETE)
//    public Msg deleteEmp(@PathVariable("id") Integer empId) {
//        employeeService.deleteEmpByEmpId(empId);
//        return Msg.success();
//    }

    /**
     * 单个批量二合一
     * 批量删除：1-2-3
     * 单个删除：1
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("ids")String ids){
        //批量删除
        if(ids.contains("-")){
            List<Integer> del_ids = new ArrayList<Integer>();
            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String string : str_ids) {
                del_ids.add(Integer.parseInt(string));
            }
            employeeService.deleteBatch(del_ids);
        }else{
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmpByEmpId(id);
        }
        return Msg.success();
    }



    /**
     *
     * 如果直接发送 ajax=put
     * 有empId过来 其余都是null
     * 但请求体中有数据 但是employee对象封装不上
     * 只有empId是更新不了的 在对应的xml中
     * 、
     * 封装不上的原因：
     *   Tomcat:
     *     将请求体中的数据，封装成一个Map
     *     2.request.getParameter("empName")就会从这个map中取值
     *     3.SpringMVC封装POJO对象的时候
     *        会把POJO每个属性的 request.getParameter("email")
     *   AJAX发送PUT请求引发的血案：
     *     PUT请求 请求体中的数据  request.getParameter("empName") 都拿不到
     *     Tomcat知道是PUT请求 就不会封装数据
     *
     *
     *     解决方法：
     *     配置HttpPUTFormContentFilter
     *
     * 员工更新方法
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    public Msg saveEmp( Employee employee) {
        System.out.println(employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }




    /**
     * 编辑单个员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable Integer id) {

        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp", employee);
    }



//
//    /**
//     * @param pn
//     * @return
//     * @ResponseBody 正常工作 需要导入JackSon包
//     */
//    @RequestMapping("/emps")
//    @ResponseBody
//    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
//
//        PageHelper.startPage(pn, 5);
//        List<Employee> emps = employeeService.getAll();
//        PageInfo page = new PageInfo(emps, 5);
//        return Msg.success().add("pageInfo",page);
//
//
//    }

    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {

        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();
        PageInfo pageInfo = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo", pageInfo);
    }


    /**
     * 1.要支持JSR303校验
     * 2.导入Hibernate—Validate
     * <p>
     * 但是POST请求时候就是保存员工
     * get  查询
     * put 修改
     * delete 删除
     *
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {

            //校验失败 应该返回失败 在模态框中显示校验失败的信息
            Map<String, Object> map = new HashMap<String, Object>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                System.out.println("错误的字段名： " + fieldError.getField());
                System.out.println("错误的信息： " + fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        } else {

        }
        employeeService.saveEmp(employee);
        return Msg.success();
    }


    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkuser")
    public Msg checkUser(String empName) {
        //先判断用户名是否是合法的；
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg", "用户名必须是6到16位数字和字母的组合 或者2到5位的中文");
        }

        //用户名合法了之后才有必要进行数据库用户名是否重复的校验
        boolean flog = employeeService.checkUser(empName);
        if (flog) {
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg","用户名不可用");
        }
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
