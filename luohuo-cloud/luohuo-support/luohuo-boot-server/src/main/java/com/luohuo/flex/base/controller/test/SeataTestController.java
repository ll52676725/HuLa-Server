package com.luohuo.flex.base.controller.test;

import com.luohuo.flex.base.service.test.SeataTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Seata事务测试控制器
 * 
 * @author 乾乾
 * @date 2025-06-08
 */
@Slf4j
@RestController
@RequestMapping("/api/test/seata")
@RequiredArgsConstructor
@Tag(name = "Seata事务测试", description = "Seata事务测试接口")
public class SeataTestController {

    private final SeataTestService seataTestService;

    /**
     * 测试Seata全局事务
     * 该接口会调用SeataTestService中的testGlobalTransaction方法，
     * 该方法会执行两个数据库操作，然后第二个操作会抛出异常，
     * 预期结果：两个操作都会回滚，数据库中不会有新的员工记录
     */
    @PostMapping("/global")
    @Operation(summary = "测试Seata全局事务", description = "测试Seata全局事务，预期两个操作都会回滚")
    public String testGlobalTransaction() {
        try {
            seataTestService.testGlobalTransaction();
            return "测试成功（未抛出异常）";
        } catch (Exception e) {
            log.error("测试Seata全局事务失败", e);
            return "测试成功（已抛出异常，事务应该回滚）";
        }
    }

    /**
     * 测试本地事务
     * 该接口会调用SeataTestService中的testLocalTransaction方法，
     * 该方法会执行两个数据库操作，然后第二个操作会抛出异常，
     * 预期结果：第一个操作会成功，第二个操作会回滚，数据库中会有一个新的员工记录
     */
    @PostMapping("/local")
    @Operation(summary = "测试本地事务", description = "测试本地事务，预期第一个操作成功，第二个操作回滚")
    public String testLocalTransaction() {
        try {
            seataTestService.testLocalTransaction();
            return "测试成功（未抛出异常）";
        } catch (Exception e) {
            log.error("测试本地事务失败", e);
            return "测试成功（已抛出异常，第一个事务应该成功，第二个事务应该回滚）";
        }
    }
}