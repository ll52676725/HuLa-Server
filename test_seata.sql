-- 创建测试表
CREATE TABLE IF NOT EXISTS test_seata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    data VARCHAR(255) NOT NULL COMMENT '测试数据',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Seata分布式事务测试表';

-- 插入测试数据
INSERT INTO test_seata (data) VALUES ('initial data');
