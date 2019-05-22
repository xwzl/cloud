package com.cloud.product.client;

import com.cloud.prodcut.common.DecreaseStockInput;
import com.cloud.prodcut.common.ProductInfoOutput;
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
@FeignClient(name = "product"/*, fallback = ProductClient.ProductClientBack.class*/)
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
    List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList);

    /**
     * 哈哈
     */
    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<DecreaseStockInput> decreaseStockInputList);

    ///**
    // * 因为order引入了依赖，所以需要对order应用配置包扫描,方法错误，进行熔断调用下面的方法
    // */
    //@Component
    //public static class ProductClientBack implements ProductClient {
    //
    //    @Override
    //    public String productMsg() {
    //        return null;
    //    }
    //
    //    @Override
    //    public List<ProductInfoOutput> listForOrder(List<String> productIdList) {
    //        return null;
    //    }
    //
    //    @Override
    //    public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {
    //
    //    }
    //}
}
