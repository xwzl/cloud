# 1、feign 调用 get 测试

### 1.1 无参数 get 请求测试

#### server 

```java
    @GetMapping("/test1")
    public String test1Get() {
        return "这是返回消息";
    }
```

#### 客户端 

```java
    @GetMapping("/params/test1")
    String test1Get();
```

#### 调用者

```java
    @GetMapping("test1")
    public void getTest1() {
        log.info(paramsDemoClient.test1Get());
    }
```

#### 测试结果

    直接通过，无任何异常

### 1.2 一个基本类型参数测试


#### server

```java
    @GetMapping("/test2")
    public String test2Get(String param1) {
        return param1;
    }
```

#### 客户端 

```java
    @GetMapping("/params/test2")
    String test2Get(String param1);
```
#### 服务调用者

```java
    @GetMapping("test2")
    public void getTest2(String param1) {
        log.info(paramsDemoClient.test2Get(param1));
    }
```

#### 测试结果

启动不报错，但是传参测试报错

- 消费者端报异常：feign.FeignException: status 405 reading ParamsDemoClient#test2Get(String)
- 服务端报异常：Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' not supported]

#### 解决办法 

客户端对应方法加上 @ParamQuest("param1") 注册，接口可以测通。

但是 server 的方法也加上 @ParamQuest("param1") 注解，服务调用者必须传入改参数，应该可以设置 @RequestParam(value = "param1", required = false) 这个注解避免此种现象。


### 1.3 两个基本类参数调用

#### server

```java
    @GetMapping("/test3")
    public String test3Get(@RequestParam(value = "param1", required = false) String param1, @RequestParam("param2") String param2) {
        return param1 + param2;
    }
```

#### 客户端 

```java
    @GetMapping("/params/test3")
    String test3Get(@RequestParam(value = "param1", required = false) String param1, @RequestParam("param2") String param2);
```
#### 服务调用者

```java
    @GetMapping("test3")
    public void getTest3(String param1, String param2) {
        log.info(paramsDemoClient.test3Get(param1, param2));
    }
```

#### 测试结果

param2 参数必须输入，否则会报 400 参数错误

param1 参数可以选择传入，证明 1.2 的猜想正确


### 1.4 一个引用类型，两个基本参数对象测试

#### server

```java
    @GetMapping("/test4")
    public String test4Get(UserDTO userDTO, @RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        return userDTO + param1 + param2;
    }
```

#### 客户端 

```java
    @GetMapping("/params/test4")
    String test4Get(@SpringQueryMap UserDTO userDTO, @RequestParam("param1") String param1, @RequestParam("param2") String param2);
```
#### 服务调用者

```java
    @GetMapping("test4")
    public void getTest4(UserDTO userDTO, String param1, String param2) {
        log.info(paramsDemoClient.test4Get(userDTO,param1, param2));
    }
```

#### 测试结果

userDTO: @SpringQueryMap 需要引入这个注解，引入@RequestBody 报请求方式不支持错误。不传参即可，默认为空对象 UserDTO(username=null, password=null, age=null, id=null)
param1和param2: 不传报 400 参数错误

### 1.5 多个引用类型对象参数

#### server
```java
    @GetMapping("/test5")
    public String test5Get(UserDTO userDTO, AppleDTO appleDTO) {
        return userDTO.toString() + appleDTO.toString();
    }
```

#### 客户端 

```java
     @GetMapping("/params/test5")
     public String test5Get(@SpringQueryMap UserDTO userDTO, @SpringQueryMap AppleDTO appleDTO);
```
#### 服务调用者

```java
    @GetMapping("test5")
    public void getTest5(UserDTO userDTO, AppleDTO appleDTO) {
        log.info(paramsDemoClient.test5Get(userDTO, appleDTO));
    }
```

#### 测试结果

userDTO 可以接收数据，appleDTO 无法接收数据。

解决思路一：

去掉第二个 @SpringQueryMap ？

直接报错

解决思路二：

全部改 @Param 注解


#### server
```java

```

#### 客户端 

```java
    @GetMapping("/params/test2")
    String test2Get(String param1);
```
#### 服务调用者

```java

```

#### 测试结果

#### server
```java

```

#### 客户端 

```java
    @GetMapping("/params/test2")
    String test2Get(String param1);
```
#### 服务调用者

```java

```

#### 测试结果

#### server
```java

```

#### 客户端 

```java
    @GetMapping("/params/test2")
    String test2Get(String param1);
```
#### 服务调用者

```java

```

#### 测试结果

#### server
```java

```

#### 客户端 

```java
    @GetMapping("/params/test2")
    String test2Get(String param1);
```
#### 服务调用者

```java

```

#### 测试结果






