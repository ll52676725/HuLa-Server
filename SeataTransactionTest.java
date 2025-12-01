package com.luohuo.test;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Seata分布式事务测试用例
 * 验证分布式事务是否能够正确回滚
 */
@Slf4j
@RestController
@RequestMapping("/seata/test")
public class SeataTransactionTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestService testService;

    /**
     * 测试分布式事务正常提交
     * @return 测试结果
     */
    @GlobalTransactional(name = "test-seata-commit", rollbackFor = Exception.class)
    @PostMapping("/commit")
    public String testCommit() {
        log.info("开始执行分布式事务提交测试");
        
        // 1. 本地数据库操作
        testService.insertLocalData("test-commit");
        
        // 2. 远程服务调用
        restTemplate.postForObject("http://luohuo-system/seata/test/remote", null, String.class);
        
        log.info("分布式事务提交测试执行完成");
        return "分布式事务提交成功";
    }

    /**
     * 测试分布式事务回滚
     * @return 测试结果
     */
    @GlobalTransactional(name = "test-seata-rollback", rollbackFor = Exception.class)
    @PostMapping("/rollback")
    public String testRollback() {
        log.info("开始执行分布式事务回滚测试");
        
        // 1. 本地数据库操作
        testService.insertLocalData("test-rollback");
        
        try {
            // 2. 远程服务调用，该调用会抛出异常
            restTemplate.postForObject("http://luohuo-system/seata/test/remote-error", null, String.class);
        } catch (Exception e) {
            log.error("远程服务调用失败，触发分布式事务回滚", e);
            throw new RuntimeException("远程服务调用失败，触发回滚");
        }
        
        return "分布式事务执行完成";
    }
}

/**
 * 测试服务类
 */
@Slf4j
@org.springframework.stereotype.Service
class TestService {

    @Autowired
    private javax.sql.DataSource dataSource;

    /**
     * 插入本地数据
     * @param data 数据内容
     */
    public void insertLocalData(String data) {
        log.info("插入本地数据: {}", data);
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO test_seata (data) VALUES (?)")) {
            ps.setString(1, data);
            ps.executeUpdate();
        } catch (java.sql.SQLException e) {
            log.error("插入本地数据失败", e);
            throw new RuntimeException("插入本地数据失败", e);
        }
    }
}