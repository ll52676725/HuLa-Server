package com.luohuo.flex.base.service.test;

import com.luohuo.flex.base.entity.user.BaseEmployee;
import com.luohuo.flex.base.service.user.BaseEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seata事务测试子服务
 * 
 * @author 乾乾
 * @date 2025-06-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeataTestChildService {

    private final BaseEmployeeService baseEmployeeService;

    /**
     * 创建一个员工然后抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void createEmployeeAndThrowException() {
        // 创建第二个员工
        BaseEmployee employee2 = new BaseEmployee();
        employee2.setEmployeeName("SeataTest2");
        employee2.setEmployeeCode("SEATA002");
        employee2.setDeptId(1L);
        employee2.setStatus(1);
        baseEmployeeService.save(employee2);
        log.info("第二个员工创建成功，ID: {}", employee2.getId());

        // 抛出异常
        throw new RuntimeException("测试Seata事务回滚");
    }
}