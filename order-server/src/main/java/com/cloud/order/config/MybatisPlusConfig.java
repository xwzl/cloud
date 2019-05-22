package com.cloud.order.config;

import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author xuweizhi
 * @since  2019-5-22
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.cloud.order.mapper.**")
public class MybatisPlusConfig {

    /**
     * SQL执行效率插件
     */
    @Bean
    //@Profile({"dev","test"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}