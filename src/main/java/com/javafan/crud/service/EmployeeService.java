package com.javafan.crud.service;


import com.javafan.crud.bean.Employee;
import com.javafan.crud.bean.EmployeeExample;
import com.javafan.crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;


    /**
     * 查询所有员工
     *
     * @return
     */
    public List getAll() {
        return employeeMapper.selectByExampleWithDept(null);
    }


    /**
     * 员工保存方法
     *
     * @param employee
     */
    public void saveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    /**
     * 检验用户名是否可用
     *
     * @param empName
     * @return true 代表该用户名可用 数据库没有该用户名
     */
    public boolean checkUser(String empName) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(empName);


        long count = employeeMapper.countByExample(example);
        return count == 0;
    }

    /**
     * 根据主键来获取员工信息
     * @param id
     * @return
     */
    public Employee getEmp(Integer id) {


        Employee employee = employeeMapper.selectByPrimaryKey(id);
        return employee;

    }

    /**
     * 删除单条数据
     * @param empId
     */
    public void deleteEmpByEmpId(Integer empId) {
         employeeMapper.deleteByPrimaryKey(empId);
    }

    /**
     * 更新单条数据
     * @param employee
     */

    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public void deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);

    }
}
