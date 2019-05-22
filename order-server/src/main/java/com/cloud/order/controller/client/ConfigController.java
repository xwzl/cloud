package com.cloud.order.controller.client;

import com.cloud.order.config.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefreshScope 配置线刷配置
 *
 * @author xuweizhi
 * @date 2019/05/22 22:36
 */
@RestController
@RefreshScope
public class ConfigController {

    @Value("${env}")
    public String env;

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping("/env")
    public String getEnv() {
        return env;
    }

    @RequestMapping("/env1")
    public GirlProperties getGirl() {
        return girlProperties;
    }

}
