# 1 Spring Cloud Hystrix

- 服务降级：
    - 优先核心服务，非核心服务不可用或弱可用
    - 通过 HystrixCommand 注解指定
    - fallbackMethod(回退函数)中具体实现降级逻辑
- 依赖隔离：
- 服务熔断
- 监控
****
# 2 服务降级

### 2.1 简介

什么是服务降级？当服务器压力剧增的情况下，根据实际业务情况及流量，对一些服务和页面有策略的不处理或换种简单的方式处理，从而释放服务器资源以保证核心交易正常运作或高效运作。

如果还是不理解，那么可以举个例子：假如目前有很多人想要给我付钱，但我的服务器除了正在运行支付的服务之外，还有一些其它的服务在运行，比如搜索、定时任务和详情等等。然而这些不重要的服务就占用了JVM的不少内存与CPU资源，为了能把钱都收下来（钱才是目标），我设计了一个动态开关，把这些不重要的服务直接在最外层拒掉，这样处理后的后端处理收钱的服务就有更多的资源来收钱了（收钱速度更快了），这就是一个简单的服务降级的使用场景。

### 2.2 使用场景

服务降级主要用于什么场景呢？当整个微服务架构整体的负载超出了预设的上限阈值或即将到来的流量预计将会超过预设的阈值时，为了保证重要或基本的服务能正常运行，我们可以将一些 不重要 或 不紧急 的服务或任务进行服务的 延迟使用 或 暂停使用。

### 2.3 核心设计

#### 分布式开关

根据上述需求，我们可以设置一个分布式开关，用于实现服务的降级，然后集中式管理开关配置信息即可。具体方案如下：

![](https://ss.csdn.net/p?https://mmbiz.qpic.cn/mmbiz_png/2p3TtlXecqbSamiaqZ09rLUxUBhKRFLXrD7Cgkuoyiad29wIAicxboYia83kpvaFYVBPlU7ChBsmJicEGdNnWHgiacAw/640?wx_fmt=png)

#### 自动降级

- **超时降级** —— 主要配置好超时时间和超时重试次数和机制，并使用异步机制探测恢复情况
- **失败次数降级** —— 主要是一些不稳定的API，当失败调用次数达到一定阀值自动降级，同样要使用异步机制探测回复情况
- **故障降级** —— 如要调用的远程服务挂掉了（网络故障、DNS故障、HTTP服务返回错误的状态码和RPC服务抛出异常），则可以直接降级
- **限流降级** —— 当触发了限流超额时，可以使用暂时屏蔽的方式来进行短暂的屏蔽

当我们去秒杀或者抢购一些限购商品时，此时可能会因为访问量太大而导致系统崩溃，此时开发者会使用限流来进行限制访问量，当达到限流阀值，后续请求会被降级；降级后的处理方案可以是：排队页面（将用户导流到排队页面等一会重试）、无货（直接告知用户没货了）、错误页（如活动太火爆了，稍后重试）。

#### 配置中心

微服务降级的配置信息是集中式的管理，然后通过可视化界面进行友好型的操作。配置中心和应用之间需要网络通信，因此可能会因网络闪断或网络重启等因素，导致配置推送信息丢失、重启或网络恢复后不能再接受、变更不及时等等情况，因此服务降级的配置中心需要实现以下几点特性，从而尽可能的保证配置变更即使达到：

![](https://ss.csdn.net/p?https://mmbiz.qpic.cn/mmbiz_png/2p3TtlXecqbSamiaqZ09rLUxUBhKRFLXr4GW0YTLRUprib8l5VTSWuPvSQdP3tviaxEmSTZIJ3EUQOrOrw5R6maiaQ/640?wx_fmt=png)

- **启动主动拉取配置** —— 用于初始化配置（减少第一次定时拉取周期）
- **发布订阅配置** —— 用于实现配置及时变更（可以解决90%左右的配置变更）
- **定时拉取配置** —— 用于解决发布订阅失效或消失丢失的情况（可以解决9%左右的发布订阅失效的消息变更）
- **离线文件缓存配置** —— 用于临时解决重启后连接不上配置中心的问题
- **可编辑式配置文档** —— 用于直接编辑文档的方式来实现配置的定义
- **提供Telnet命令变更配置** —— 用于解决配置中心失效而不能变更配置的常见

#### 处理策略

当触发服务降级后，新的交易再次到达时，我们该如何来处理这些请求呢？从微服务架构全局的视角来看，我们通常有以下是几种常用的降级处理方案：

- **页面降级** —— 可视化界面禁用点击按钮、调整静态页面
- **延迟服务** —— 如定时任务延迟处理、消息入MQ后延迟处理
- **写降级** —— 直接禁止相关写操作的服务请求
- **读降级** —— 直接禁止相关度的服务请求
- **缓存降级** —— 使用缓存方式来降级部分读频繁的服务接口

针对后端代码层面的降级处理策略，则我们通常使用以下几种处理措施进行降级处理：

- 抛异常
- 返回NULL
- 调用Mock数据
- 调用Fallback处理逻辑

### 高级特性
我们已经为每个服务都做好了一个降级开关，也已经在线上验证通过了，感觉完全没问题了。


场景一：某一天，运营搞了一次活动，突然跑过来说，现在流量已经快涨到上限了，有没有批量降级所有不重要服务的方式？开发一脸懵逼的看着，这又不是操作DB，哪里有批量操作呀。

场景二：某一天，运营又搞事了，说我们等下要搞一个活动，让我们赶紧提前把不重要的服务都降级了，开发又是一脸懵逼，我怎么知道要降级哪些服务呀。

反思：服务降级的功能虽然是实现了，可是没有考虑实施时的体验。服务太多，不知道该降级哪些服务，单个操作降级速度太慢……

### 分级降级

当微服务架构发生不同程度的情况时，我们可以根据服务的对比而进行选择式舍弃（即丢车保帅的原则），从而进一步保障核心的服务的正常运作。

如果等线上服务即将发生故障时，才去逐个选择哪些服务该降级、哪些服务不能降级，然而线上有成百上千个服务，则肯定是来不及降级就会被拖垮。同时，在大促或秒杀等活动前才去梳理，也是会有不少的工作量，因此建议在开发期就需要架构师或核心开发人员来提前梳理好，是否能降级的初始评估值，即是否能降级的默认值。

为了便于批量操作微服务架构中服务的降级，我们可以从全局的角度来建立服务重要程度的评估模型，如果有条件的话，建议可以使用 层次分析法（The analytic hierarchy process，简称AHP） 的数学建模模型（或其它模型）来进行定性和定量的评估（肯定比架构师直接拍脑袋决定是否降级好很多倍，当然难度和复杂度也会高许多，即你需要一个会数学建模人才），而层次分析法的基本思路是人对一个复杂的决策问题的思维和判断过程大体上是一样的。

以下是个人给出的最终评价模型，可作为服务降级的评价参考模型进行设计：

我们利用数学建模的方式或架构师直接拍脑袋的方式，结合服务能否降级的优先原则，并根据台风预警（都属于风暴预警）的等级进行参考设计，可将微服务架构的所有服务进行故障风暴等级划分为以下四种：

#### 评估模型：

- 蓝色风暴 —— 表示需要小规模降级非核心服务
- 黄色风暴 —— 表示需要中等规模降级非核心服务
- 橙色风暴 —— 表示需要大规模降级非核心服务
- 红色风暴 —— 表示必须降级所有非核心服务

#### 计说明：

故障严重程度为：蓝色＜黄色＜橙色＜红色

建议根据二八原则可以将服务划分为：80%的非核心服务+20%的核心服务

以上模型只是整体微服务架构的服务降级评估模型，具体大促或秒杀活动时，建议以具体主题为中心进行建立（不同主题的活动，因其依赖的服务不同，而使用不同的进行降级更为合理）。当然模型可以使用同一个，但其数据需要有所差异。最好能建立一套模型库，然后实施时只需要输入相关服务即可输出最终降级方案，即输出本次大促或秒杀时，当发生蓝色风暴时需要降级的服务清单、当发生黄色风暴时需要降级的服务清单……

#### 降级权值

微服务架构中有服务权值的概念，主要用于负载时的权重选择，同样服务降级权值也是类似，主要用于服务降级选择时的细粒度优先级抉择。所有的服务直接使用以上简单的四级划分方式进行统一处理，显然粒度太粗，或者说出于同一级的多个服务需要降级时的 降级顺序 该如何？甚至我想要人工智能化的 自动降级，又该如何更细粒度的控制？

基于上述的这些AI化的需求，我们可以为每一个服务分配一个降级权值，从而便于更加智能化的实现服务治理。而其评估的数值，同样也可以使用数学模型的方式进行 定性 与 定量 的评估出来，也可以架构师根据经验直接拍脑袋来确定。

### 服务降级

```java
/**
 * 服务降级默认触发的方法：@DefaultProperties {@link #defaultFallback}
 *
 * @author xuweizhi
 * @date 2019/05/24 11:31
 */
@RestController
@RequestMapping("/hystrix")
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 服务降级，方法体中服务服务出现问题，调用fallback 进行错误提示,可用于抛出异常,考虑更多的是时间的延迟，默认的延迟时间是一秒触发降级
     * HystrixCommandProperties default_executionTimeoutInMilliseconds 中设置超时时间为3000ms 3s内服务不降级处理 配置文件也可配
     * 置。
     * <p>
     * 此外方法出现异常也可触发降级服务,服务降级方法返回值与方法返回值一致
     */
    @HystrixCommand(fallbackMethod = "fallBack")
    @GetMapping("/fallback")
    public String getProductInfo() {
        ServiceInstance product = loadBalancerClient.choose("PRODUCT");
        String url = String.format("http://%s:%s/product/list", product.getHost(), product.getPort());
        RestTemplate restTemplate = new RestTemplate();
        if (Math.random() > 0.5) {
            throw new RuntimeException("服务内部出错也会引发降级!");
        }
        // 目标服务出现异常或者请求超时将会触发服务降级
        ResultVO forObject = restTemplate.getForObject(url, ResultVO.class);
        return forObject.toString();
    }

    /**
     * 记得加 HystrixCommand 注解
     */
    @HystrixCommand
    @GetMapping("/defaultFallback")
    public String defaultFault() {
        if (Math.random() > 0.5) {
            throw new RuntimeException("服务内部出错也会引发降级!");
        }
        return "服务局降级默认方法测试";
    }

    /**
     * 请求超时触发降级,点进 @HystrixProperty 包，进入 hystrix 包，找到 {@link HystrixCommandProperties} 获取其中想要设置的属性值
     * 比如超时时间设置：双击属性，找到键值对 execution.isolation.thread.timeoutInMilliseconds 。
     */
    @HystrixCommand(commandProperties = {
            // 设置超时时间为 3 s，默认 1 秒触发降级服务
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000")
    })
    @GetMapping("/listFor")
    public String listFor() {
        RestTemplate restTemplate = new RestTemplate();
        List result = restTemplate.getForObject("http://product:/product/listFor", List.class);
        assert result != null;
        return result.toString();
    }

    /**
     * 服务降级会触发此方法
     */
    @NotNull
    @Contract(pure = true)
    private String fallBack() {
        return "服务降级会触发此方法！";
    }

    @NotNull
    @Contract(pure = true)
    private String defaultFallback() {
        return "服务降级默认触发的方法";
    }
}
```

### 依赖隔离

Hystrix 实现线程池的隔离，会为每个 HystrixCommand 创建一个独立的线程池，所以不会影响其它服务。

Hystrix 自动实现了依赖隔离。

### 服务熔断

断路器模式是将受保护的服务封装到一个可以监控故障的短路器对象里面，当故障达到一定的值，断路器将会
跳闸，断路器对象返回错误。

![](image/1.png)

断路器模式设计状态机，状态机模式有三种状态：

- closed: 熔断器的关闭状态，调用失败次数累计到了一定的阈值或者一定的比例，启动熔断机制，进入熔断打开状态。
- open: 熔断器打开状态，对于任何服务都返回错误状态。设计了一个时钟选项，默认设置的时钟过去后，熔断器会进入半熔断状态。
- half open: 允许定量的服务请求，如果调用都成功或者一定的比例，那么会关闭熔断器。如果失败，那么回到熔断器打开状态。 

sleepWindowInMilliseconds： 时间窗口，断路器确定是否需要打开，统计一些请求或者错误数据的时候，有一个时间范围，这个时间
范围就是时间窗口。当断路器打开，对主逻辑进行熔断的时候，Hystrix 会启动一个休眠时间窗，在这个时间窗内，降级逻辑会临时成为这
个逻辑的主逻辑。当休眠时间窗到期，断路器将进入半开状态，释放一些请求到原来的主逻辑上面。如果此次请求能够正常返回，那么断路器
将会被闭合，主逻辑恢复。如果这次请求依然有问题，断路器将继续进入断路器打开状态，休眠时间窗重新计时。

circuitBreaker.requestVolumeThreshold: 滚动窗口最小请求数 10 s;
circuitBreaker.errorThresholdPercentage: 表示滚动窗口请求的数量错误在 7 次以上，将打开熔断状态。 >= 60%

```java
 /**
     * 服务熔断：重试机制和断路器模式 一般是某个服务故障或异常引起，类似“保险丝”，当某个异常被触发，直接熔断整个服务，而不是等到此服务超时。
     *
     * 熔断现象:当个接口的错误率到达 60，其正确的请求服务也会被拒绝
     *
     * http://localhost:8081/hystrix/serviceBlow?number=1 短时间内请求多次，达到阈值，会进行熔断
     *
     * http://localhost:8081/hystrix/serviceBlow?number=2 熔断之后，访问失效
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),                    // 设置熔断为打开状态
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),       // 滚动窗口最小请求数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 断路器打开时间窗口
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")      // 60 表示失败60%的次数，断路器会被打开
    })
        @GetMapping("/serviceBlow")
    public String serviceBlow(@RequestParam("number") Integer number) {
        if (number % 2 == 0) {
            return "success";
        }
        RestTemplate restTemplate = new RestTemplate();
        List result = restTemplate.getForObject("http://localhost:8080/product/listFor", List.class);
        assert result != null;
        return result.toString();
    }
```

### Feign 配置 hystrix 

```java 

# feign 熔断措施
feign:
  hystrix:
    enabled: true
 
// 启动类配置包扫描   
@SpringCloudApplication
@EnableSwagger2
@EnableScheduling
@EnableCaching
@EnableFeignClients(basePackages = "com.cloud.product.client")
@ComponentScan(basePackages = {"com.cloud.product.client","com.cloud.order","com.cloud.common"})
@MapperScan("com.cloud.order.mapper")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}

// Feign 配置熔断回调方法
/**
 * Feign 方式访问服务,name 属性表示访问的服务名称
 *
 * @author xuweizhi
 * @date 2019/05/22 11:33
 */
@FeignClient(name = "product", fallback = ProductClient.ProductClientBack.class)
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

    /**
     * 如果产生服务降级，将会调用下面的方法，在引入该方法的内部一定要加包扫描
     */
    @Component
    class ProductClientBack implements ProductClient {

        @Override
        public String productMsg() {
            return "productMsg 服务降级 !";
        }

        @Override
        public List<ProductInfoOutput> listForOrder(List<String> productIdList) {
            return null;
        }

        @Override
        public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {

        }
    }

}

```

### hystrix 监控平台

![](image/2.png)

登录后无法访问，修改配置文件

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*" # 开放所有页面节点  默认只开启了health、info两个节点
        # 配置后监控路径为 http://localhost:8081/hystrix.stream,默认 http://localhost:8081/actuator/hystrix.stream
      base-path: /   # 修改访问路径  2.0之前默认是/   2.0默认是 /actuator  可以通过这个属性值修改
  server:
    port: 8081 #actuator端口
#    servlet: # 无效的配置
#      context-path: / # 同 base-path
```

![](image/3.png)