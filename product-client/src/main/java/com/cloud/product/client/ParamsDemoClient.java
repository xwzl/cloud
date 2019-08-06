package com.cloud.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuweizhi
 * @since 2019-08-06
 */
@FeignClient(name = "product")
public interface ParamsDemoClient {

    @GetMapping("/params/test1")
    public String test1Get();

    @GetMapping("/params/test2")
    public String test2Get(@RequestParam("param1") String param1);

    @GetMapping("/test3")
    public String test3Get(@RequestParam("param1") String param1, @RequestParam("param2") String param2);


}
