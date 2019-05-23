package com.cloud.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
@EnableCaching
@MapperScan("com.cloud.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
