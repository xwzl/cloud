package com.cloud.order.client;

import com.cloud.order.dataobject.ProductInfo;
import com.cloud.order.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign 方式访问服务,name属性表示访问的服务名称
 *
 * @author xuweizhi
 * @date 2019/05/22 11:33
 */
@FeignClient(name = "product")
public interface ProductClient {

    /**
     * 获取 product 服务的接口数据
     *
     * @return 获取数据
     */
    @PostMapping("/msg")
    String productMsg();

    /**
     * 调用远程服务,@RequestBody 注解使用后必须用 post
     */
    @PostMapping("/product/listForOrder")
    List<ProductInfo> listForOrder(@RequestBody List<String> productIdList);

    /**
     * 哈哈
     */
    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDTO> decreaseStockInputList);
}
