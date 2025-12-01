package com.luohuo.flex.base.service.test;

import com.luohuo.flex.base.entity.user.BaseEmployee;
import com.luohuo.flex.base.service.user.BaseEmployeeService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seata事务测试服务
 * 
 * @author 乾乾
 * @date 2025-06-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeataTestService {

    private final BaseEmployeeService baseEmployeeService;
    private final SeataTestChildService seataTestChildService;

    /**
     * 测试Seata全局事务
     * 该方法会执行两个数据库操作：
     * 1. 创建一个员工（成功）
     * 2. 调用子服务的方法，该方法会创建一个员工然后抛出异常（失败）
     * 预期结果：两个员工都不会被创建，事务会回滚
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public void testGlobalTransaction() {
        // 创建第一个员工
        BaseEmployee employee1 = new BaseEmployee();
        employee1.setEmployeeName("SeataTest1");
        employee1.setEmployeeCode("SEATA001");
        employee1.setDeptId(1L);
        employee1.setStatus(1);
        baseEmployeeService.save(employee1);
        log.info("第一个员工创建成功，ID: {}", employee1.getId());

        // 调用子服务的方法，该方法会创建一个员工然后抛出异常
        seataTestChildService.createEmployeeAndThrowException();
    }

    /**
     * 测试本地事务
     * 该方法会执行两个数据库操作：
     * 1. 创建一个员工（成功）
     * 2. 调用子服务的方法，该方法会创建一个员工然后抛出异常（失败）
     * 预期结果：第一个员工会被创建，第二个员工不会被创建，只有子服务的事务会回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public void testLocalTransaction() {
        // 创建第一个员工
        BaseEmployee employee1 = new BaseEmployee();
        employee1.setEmployeeName("LocalTest1");
        employee1.setEmployeeCode("LOCAL001");
        employee1.setDeptId(1L);
        employee1.setStatus(1);
        baseEmployeeService.save(employee1);
        log.info("第一个员工创建成功，ID: {}", employee1.getId());

        // 调用子服务的方法，该方法会创建一个员工然后抛出异常
        seataTestChildService.createEmployeeAndThrowException();
    }
}