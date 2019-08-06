package com.cloud.product.controller.server;

import com.cloud.common.dtos.AppleDTO;
import com.cloud.common.dtos.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String test2Get(@RequestParam(value = "param1", required = false) String param1) {
        return param1;
    }

    @GetMapping("/test3")
    public String test3Get(@RequestParam(value = "param1", required = false) String param1, @RequestParam("param2") String param2) {
        return param1 + param2;
    }

    @GetMapping("/test4")
    public String test4Get(UserDTO userDTO, @RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        return userDTO + param1 + param2;
    }

    @GetMapping("/test5")
    public String test5Get(UserDTO user, AppleDTO apple) {
        return user.toString() + apple.toString();
    }

    @GetMapping(value = "/test6")
    public String test6Get(List<AppleDTO> lists) {
        return lists.toString();
    }

    @PostMapping("/test7")
    public String test6Get(String param1, String param2) {
        return param1 + ":" + param2;
    }

    @PostMapping("/test8")
    public String test8Get(@RequestBody List<AppleDTO> lists) {
        return lists.toString();
    }

    @PostMapping("/test9")
    public String test9Get(@RequestBody UserDTO user) {
        return user.toString() ;
    }
}
