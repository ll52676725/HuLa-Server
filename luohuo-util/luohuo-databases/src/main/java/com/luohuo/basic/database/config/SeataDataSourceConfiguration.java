package com.luohuo.basic.database.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.luohuo.basic.database.properties.DatabaseProperties;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Seata数据源代理配置类
 * 当luohuo.database.is-seata=true时启用
 */
@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@ConditionalOnProperty(prefix = "luohuo.database", name = "is-seata", havingValue = "true")
@AutoConfigureAfter(DefaultDataSourceCreator.class)
public class SeataDataSourceConfiguration {

    @Bean
    @Primary
    public DataSource dataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
        return new DataSourceProxy(dynamicRoutingDataSource);
    }
}