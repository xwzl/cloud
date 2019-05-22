# 1、Linux 部署项目
    
    # pull 项目
    git clone https://github.com/xwzl/cloud.git
    
    # 运行项目
    cd ./cloud/spring-server
    
    # 单机，一般用这个
     docker run -e "SPRING_PROFILES_ACTIVE=server4" -p 8761:8761 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server -d cloud/spring-server:1.0.0  
    
    # 集群
    docker run -e "SPRING_PROFILES_ACTIVE=server1" -p 8761:8761 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server1 -d cloud/spring-server:1.0.0
    docker run -e "SPRING_PROFILES_ACTIVE=server2" -p 8762:8762 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server2 -d cloud/spring-server:1.0.0
    docker run -e "SPRING_PROFILES_ACTIVE=server3" -p 8763:8763 -e "ES_JAVA_OPTS=-Xms64m -Xmx64m" --name server3 -d cloud/spring-server:1.0.0  
    
# 2、Eureka 总结
 
### 2.1 分布式系统中为什么需要服务发现
 
当A 服务需要 B 服务提供功能时，需要配置 B 服务的接口配置信息，达到应用间通信的目的，但是 B 服务部署的主机数量过多的话，A 服务的配置项会增加，最主要
的原因是 B 服务提供服务是动态变化的。因此注册中心的出现解决了这个问题，注册中心为 B 服务启动时提供注册功能，当 A 服务调用 B 服务时，注册中心分配给 A 
一个可用的 B 服务。

#### A 获取 B 服务的两种方式：

客户端发现：A 需要服务时，注册中心给 A 需要服务的所有注册地址，A 采用轮询、随机、负载均衡的方式获取其中一个地址。
服务端发现：服务端采用代理的方式，通过轮询、随机、负载均衡的方式分配从众多可用的服务一个地址给 A 服务。常见的服务端发现 Nginx,Zookper,Kubernetes

Eureka 客户端发现 

### 2.2 特点
 
- 客户端和服务端引入依赖不相同，服务端启动类需要添加@EnableEurekaServer，客户端启动类添加@EableEurekaClient;
- Eureka 心跳检测、健康检查、负载均衡功能等;
- Eureka 的高可用，生产上建议至少两台以上；
- 分布式系统中，服务注册中心是最重要的部分。

# 3、微服务的拆分

- 每个微服务都有单独的数据存储，微服务之间共享数据库，会产生紧密耦合。如果不遵循，更改一个微服务数据的表结构，将会影响其他
服务的，应该通过接口调用获取数据。
- 依据服务特点选择不同结构的数据库类型。
- 难点在确定边界，针对边界设计API，依据边界权衡数据冗余。

# 4、应用间通信

### HTTP vs RPC
    
- dubbo
- Spring Cloud

### SpringCloud 中服务间两种 restful 调用方式

- RestTemplate
- Feign

### 客户端负载均衡：Ribbon

Eureka 是客户端发现的方式，他的负载均衡是软负载，客户单向服务器拉取已注册的可用服务信息，根据负载均衡策略直接命中服务器
发起请求。整个过程都在客户端完成，并不需要服务器参与。

- RestTemplate
- Feign
- Zuul

#### Ribbon 实现软负载均衡核心有三点：

- 服务发现：发现依赖服务的列表，依据服务的名字寻早该服务的所有实例 
- 服务选择规则：从多个服务中选取一个有效的服务
- 服务监听： 检测失效的服务，做到高效剔除

#### 组件

- ServerList 
- IRule
- ServerListFilter

通过 ServerList 获取所有的可用服务列表，然后 ServerListFilter 过滤一部分地址，最后 IRule 获取一个可用的服务。
    
#### 服务通信 feign

调用方（client）配置轮询规则

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
#### 注意事项

Feign 调用时，@RequestBody 必须用 Post方法，@PathVariable @RequestParam 或者无参数时可以用get 


#### 多模块

- server: 所有业务逻辑
- client: 对外暴露所有的接口
- common：公用的对象

# 3. 统一配置中心

配置不方便维护，配置内容安全与权限，更新配置项目重启