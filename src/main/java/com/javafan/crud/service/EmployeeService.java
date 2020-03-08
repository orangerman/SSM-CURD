package com.javafan.crud.service;


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
     * @return
     */
    public List getAll() {
        return employeeMapper.selectByExampleWithDept(null);
    }
}
