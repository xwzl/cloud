package com.cloud.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 服务端注册中心，@EnableDiscoveryClient
 *
 * @author xuweizhi
 * @date 2019/05/20 0:25
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@EnableScheduling
@EnableFeignClients(basePackages = "com.cloud.product.client")
@MapperScan("com.cloud.order.mapper")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
