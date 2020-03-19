package com.javafan.crud.test;


import com.javafan.crud.bean.Department;
import com.javafan.crud.bean.Employee;
import com.javafan.crud.bean.Msg;
import com.javafan.crud.dao.DepartmentMapper;
import com.javafan.crud.dao.EmployeeMapper;
import com.javafan.crud.service.DepartmentService;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

/**
 * 测试Dao层
 * 推荐Spring的项目就可以使用Spring的单元测试
 * 1.导入SpringTest模块
 * 2.@ContextConfiguration指定spring配置文件的数据
 * 3.直接autowired要使用的组件
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlSession;

    @Autowired
    DepartmentService departmentService;


    @Test
    public void testCrud() {
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        DepartmentMapper departmentMapper = ctx.getBean(DepartmentMapper.class);
//        System.out.println(departmentMapper);
        System.out.println(departmentMapper);
        //1.插入几个部门
//        departmentMapper.insertSelective(new Department(null, "开发部"));
//        departmentMapper.insertSelective(new Department(null, "测试部"));

        //2.生成员工数据，测试员工插入
//        employeeMapper.insertSelective(new Employee(null, "Tom", "M", "Tom@163.com", 1));
//        employeeMapper.insertSelective(new Employee(null, "Jerry", "M", "Jerry@163.com", 2));
        //3.批量插入多个员工:批量 使用可以执行批量操作的SqlSession
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        for (int i = 0; i < 1000; i++) {
//            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
//            mapper.insertSelective(new Employee(null, uid, "M", uid + "@163.com", 1));
//        }
//        System.out.println("批量完成");

        Employee employee = employeeMapper.selectByPrimaryKeyWithDept(1);
        System.out.println(employee);
        System.out.println("*******************");
        Employee employee1 = employeeMapper.selectByPrimaryKey(1);
        System.out.println(employee1);

    }


    @Test
    public void testDepartmentService() {
//        List<Department> list = departmentMapper.selectByExample(null);
//        System.out.println(list);
        List<Department> depts = departmentService.getDepts();
//        System.out.println(depts);
        Msg ms = Msg.success().add("depts", depts);
        System.out.println(ms);

    }


}
