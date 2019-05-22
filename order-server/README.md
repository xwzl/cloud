# 服务通信

配置轮询规则
```yml
# 负载均衡策略，默认使用轮询，shirt 双击，搜索 IRule ，配置其实现类
PRODUCT:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```
在调用方新增  "org.springframework.cloud:spring-cloud-starter-openfeign"

启动类上新增注解 @EnableFeignClients

```java
@FeignClient(name = "product")
@RequestMapping("/server")
public interface ProductClient {

    /**
     * 获取 product 服务的接口数据
     *
     * @param msg msg 参数
     * @return 获取数据
     */
    @GetMapping("/msg")
    String productMsg(String msg);

}
```