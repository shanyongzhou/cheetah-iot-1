package com.cheetah.common.core.config;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 全局mybatis配置
 */
@Configuration
@MapperScan(basePackages = {"com.cheetah.**.dao", "hxgroup.**.mapper"})
public class MyBatisConfig {
    /**
     * 添加数据库id支持，替换默认的 provider
     * @return
     */
    @Bean
    VendorDatabaseIdProvider databaseIdProvider(){
        VendorDatabaseIdProvider vendorDatabaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("Oracle","oracle");
        properties.put("MySQL","mysql");
        vendorDatabaseIdProvider.setProperties(properties);
        return vendorDatabaseIdProvider;
    }
}
