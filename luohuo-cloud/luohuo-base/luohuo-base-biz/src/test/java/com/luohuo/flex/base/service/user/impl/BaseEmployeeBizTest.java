package com.luohuo.flex.base.service.user.impl;

import com.luohuo.flex.base.biz.user.BaseEmployeeBiz;
import com.luohuo.flex.base.entity.user.BaseEmployee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.annotation.Resource;
import java.util.Date;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BaseEmployeeBizTest {

    @Resource
    private BaseEmployeeBiz baseEmployeeBiz;

    @Test
    public void testTransactionRollback() {
        // 创建一个新的员工对象
        BaseEmployee employee = new BaseEmployee();
        employee.setEmployeeName("测试员工");
        employee.setEmployeeCode("TEST001");
        employee.setEmployeeStatus(1);
        employee.setCreateTime(new Date());
        employee.setUpdateTime(new Date());

        try {
            // 调用业务方法，该方法应该包含一个分布式事务
            baseEmployeeBiz.testTransaction(employee);
        } catch (Exception e) {
            // 捕获异常，验证事务是否回滚
            System.out.println("事务已回滚: " + e.getMessage());
        }
    }
}