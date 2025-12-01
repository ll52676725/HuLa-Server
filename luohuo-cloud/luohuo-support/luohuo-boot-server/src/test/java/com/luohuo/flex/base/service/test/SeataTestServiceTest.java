package com.luohuo.flex.base.service.test;

import com.luohuo.basic.database.mybatis.conditions.Wraps;
import com.luohuo.flex.base.entity.user.BaseEmployee;
import com.luohuo.flex.base.service.user.BaseEmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Seata事务测试类
 * 
 * @author 乾乾
 * @date 2025-06-08
 */
@SpringBootTest
@Transactional
public class SeataTestServiceTest {

    @Autowired
    private SeataTestService seataTestService;

    @Autowired
    private BaseEmployeeService baseEmployeeService;

    private int initialEmployeeCount;

    @BeforeEach
    public void setUp() {
        // 记录初始员工数量
        initialEmployeeCount = baseEmployeeService.count();
    }

    @AfterEach
    public void tearDown() {
        // 清理测试数据
        List<BaseEmployee> testEmployees = baseEmployeeService.list(
                Wraps.<BaseEmployee>lbQ()
                        .like(BaseEmployee::getEmployeeCode, "SEATA%")
                        .or()
                        .like(BaseEmployee::getEmployeeCode, "LOCAL%")
        );
        if (!testEmployees.isEmpty()) {
            baseEmployeeService.removeByIds(testEmployees.stream().map(BaseEmployee::getId).collect(Collectors.toList()));
        }
    }

    /**
     * 测试Seata全局事务
     * 预期结果：两个员工都不会被创建，事务会回滚
     */
    @Test
    public void testGlobalTransaction() {
        // 调用测试方法
        try {
            seataTestService.testGlobalTransaction();
        } catch (Exception e) {
            // 预期会抛出异常
        }

        // 验证员工数量是否没有变化
        int currentEmployeeCount = baseEmployeeService.count();
        assertThat(currentEmployeeCount).isEqualTo(initialEmployeeCount);

        // 验证是否有SEATA开头的员工代码
        List<BaseEmployee> seataEmployees = baseEmployeeService.list(
                Wraps.<BaseEmployee>lbQ()
                        .like(BaseEmployee::getEmployeeCode, "SEATA%")
        );
        assertThat(seataEmployees).isEmpty();
    }

    /**
     * 测试本地事务
     * 预期结果：第一个员工会被创建，第二个员工不会被创建，只有子服务的事务会回滚
     */
    @Test
    public void testLocalTransaction() {
        // 调用测试方法
        try {
            seataTestService.testLocalTransaction();
        } catch (Exception e) {
            // 预期会抛出异常
        }

        // 验证员工数量是否增加了1
        int currentEmployeeCount = baseEmployeeService.count();
        assertThat(currentEmployeeCount).isEqualTo(initialEmployeeCount + 1);

        // 验证是否有LOCAL开头的员工代码
        List<BaseEmployee> localEmployees = baseEmployeeService.list(
                Wraps.<BaseEmployee>lbQ()
                        .like(BaseEmployee::getEmployeeCode, "LOCAL%")
        );
        assertThat(localEmployees).hasSize(1);

        // 验证是否有SEATA开头的员工代码
        List<BaseEmployee> seataEmployees = baseEmployeeService.list(
                Wraps.<BaseEmployee>lbQ()
                        .like(BaseEmployee::getEmployeeCode, "SEATA%")
        );
        assertThat(seataEmployees).isEmpty();
    }
}