package com.cloud.product.controller.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuweizhi
 * @since 2019-08-06
 */
@RestController
@RequestMapping("/params")
public class ParamsController {


    @GetMapping("/test1")
    public String test1Get() {
        return "这是返回消息";
    }

    /**
     * 。 @RequestBody + @RequestLParam 同时添加
     *
     * @param param1
     * @return
     */
    @GetMapping("/test2")
    public String test2Get(@RequestParam("param1") String param1) {
        return param1;
    }

    @GetMapping("/test3")
    public String test3Get(@RequestParam("param1") String param1,@RequestParam("param2") String param2) {
        return param1+param2;
    }
}
