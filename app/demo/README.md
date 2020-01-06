# Demo 示例
## 0.概述
用于演示 `mdc-cloud` 微服务应用实际开发的示例代码

## 1.依赖和配置
### 1.1 依赖
依赖引入只以 `maven` 为例
* 必要依赖

|groupId|artifactId|描述|作用|
|---|---|---|---|
|org.springframework.cloud|spring-cloud-starter-netflix-eureka-client|eureka 客户端|服务注册、服务发现|
|org.springframework.cloud|spring-cloud-starter-config|config 客户端|获取配置中心的配置文件，并根据获取的配置更新应用|
|org.springframework.cloud|spring-cloud-starter-bus-kafka|消息总线的 kafka 实现|用于接受配置中心推送，实时更新配置文件|
|com.alibaba.cloud|spring-cloud-starter-alibaba-sentinel|alibaba sentinel哨兵节点|实时监控、管理服务和应用的流量，同时可用于熔断、服务降级|
|com.cheetah.common|core|mdc-cloud 核心组件|提供标准的系统通用功能和配置|

* 可选依赖

此处只列出开发常用的依赖，如需要其他第三方，请在 [`cloud` 项目根目录的pom](/pom.xml)中添加。

|groupId|artifactId|描述|作用|
|---|---|---|---|
|org.springframework.cloud|spring-cloud-starter-openfeign|open feign|提供集成了负载均衡的http调用|
|org.mybatis.spring.boot|mybatis-spring-boot-starter|mybatis 提供的 spring boot stater|整合mybatis和springboot，可以在spring boot中更方便地使用mybatis|
|com.cheetah.common|web|mdc-cloud web应用组件|定义服务间交互的规则和标准|
|org.apache.zookeeper|zookeeper|zookeeper client 的java实现|用于与 `zookeeper` 服务交互|
|com.oracle|ojdbc6|oracle 数据库 jdbc 驱动|使用 `oracle` 数据库作为数据源时提供jdbc驱动|
|mysql|mysql-connector-java|使用 `MySQL` 数据库作为数据源时提供jdbc驱动|

### 1.2 配置
所有微服务的配置都应尽量使用配置中心统一集中管理，如果不想给配置中心管理，可以使用 zookeeper 存储。

除非必要，**不要**使用本地配置。
* 远程配置示例

放置于配置中心的配置仓库，使用 
>{application_name}-{profile}.yml 

或 
>{application_name}-{profile}.properties

作为文件名。

具体参数参考 [demo-dev.yml](/configRepo/demo-dev.yml)

* 本地配置示例

文件名称必须为
>bootstrap.yml

或
>bootstrap.properties

参考本示例的 [bootstrap.yml](/app/demo/src/main/resources/bootstrap.yml)

* **日志由于需要统一收集处理，务必使用以下配置**
    * `log4j2.component.propertis` 整个文件拷贝到项目 `resource` 目录下。
    * `bootstrap.yml` 文件中配置
    ```yaml
    logging:
      config: http://localhost:8888/*/dev/*/log4j.yml
    ```
  
## 2. programming guide
### 2.1 包命名和划分规范
* dao

由于 mapper interface 扫描的需要，必须以 `com.cheetah`开始，并以 `dao`结尾，示例：
>com.cheetah.cloud.app.demo.dao

dao包中只允许放置定义为 interface 的 mapper 接口，不允许放置class/abstract class/enum 等非java接口的文件。
* entity

用于定义只在本应用范围内使用的实体。
如果是与其他应用或服务交互的实体，需要定义到 [web](/common/web) 组件中。

### 2.2 其他非java代码资源
* resource/mapper

该目录用于放置所有的 mapper.xml 文件。
* README.md 

应用的说明文档，与本文档类似，描述和说明此应用的职责、使用依赖、开发规范等信息。

### 2.3 SpringBootApplication
通常一个应用以 SpringBoot 为基础开发，在源代码根目录下创建应用启动类，如 [`DemoApplication.java`](/app/demo/src/main/java/cn/hexing/cloud/app/demo/DemoApplication.java)

在此类上添加如下注解:
```yaml
@SpringBootApplication(scanBasePackages = {"com.cheetah.**"})  //将com.cheetah.** 作为spring component扫描的包
@EnableFeignClients   //不必须，使用open feign 需添加 
@EnableCaching    //启用缓存，必须，否则可能无法对某些公用数据进行本地缓存，严重影响效率
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 2.4 Controller
`mdc-cloud` 使用 alibaba sentinel 作为流量控制手段，在需要监控流量的接口上需要添加如下注解：
```java
    @GetMapping("/options")  // 使用 get 方法的请求路径
    @SentinelResource(value = "options",  // 服务名称，和应用名称一起用于在 sentinel 上标识一个服务
            defaultFallback = "defaultFallbackForRateLimit",  // 当异常达到阈值时默认的回调函数
            blockHandlerClass = ExceptionUtil.class, exceptionsToIgnore = { //容错规则忽略的异常，如果不希望某些异常被作为服务异常的判断标准，需要在这里剔除
                BusinessRuntimeException.class,
                BusinessException.class}) 
    public ResponseData getOptions(String code) {
       //function ...
    }
```

### 2.5 Cache 
core 组件集成了spring cache，本地使用内存 ConcurrentMap作为缓存，整体使用redis作为一级缓存，使用缓存数据的方法和普通方法一致，直接调用：
```java
@Autowired
private SystemConfigCache systemConfigCache;

void function(){
//...
systemConfigCache.getMDCSystemConfigValue(code);
//...
}
```

### 2.6 feign
open feign 集成了负载均衡，通常情况使用默认配置进行开发。
使用时需要定义远程 RESTful 接口:
```java
@FeignClient("mdcbootstrap")
public interface SystemConfigService {
    @PostMapping("config/refresh")
    ResponseData refreshConfiguration();
}
```
然后直接使用：
```java
@Autowired
private SystemConfigService systemConfigService;

//....
systemConfigService.refreshConfiguration();
//....
```

### 2.7 日志收集
log4j2的配置统一于配置中心的 [`log4j-dev.yml`](/configRepo/log4j-dev.yml) 文件。
本地不需要做额外修改。

使用时推荐使用 `org.slf4j.Logger` :
```java
private static final Logger logger = LoggerFactory.getLogger(ConfigDemoController.class);

//...
logger.info("log something");
//...
```