package com.cloud.user.controller;

import com.cloud.product.client.ParamsDemoClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuweizhi
 * @since 2019-08-06
 */
@RestController
@RequestMapping("/feign")
@Slf4j
@Api("服务间调用参数测试")
public class FeignDemosController {

    @Autowired
    private ParamsDemoClient paramsDemoClient;

    @GetMapping("test1")
    public void getTest1() {
        log.info(paramsDemoClient.test1Get());
    }

    @GetMapping("test2")
    public void getTest1(String param1) {
        log.info(paramsDemoClient.test2Get(param1));
    }

    @GetMapping("test3")
    public void getTest1(String param1,String param2) {
        log.info(paramsDemoClient.test3Get(param1,param2));
    }

}
