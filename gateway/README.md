# Gateway 统一网关
## 0. 概述
`gateway` 是 mdc-cloud 的统一网关，所有外部访问都需要经过 `gateway`。

`gateway` 基于 `spring-cloud-gateway` 构建，集成了 UAP 平台鉴权功能，支持负载均衡，未来将支持服务降级和限流。

## 1. 编译和启动
### 1.1 编译
使用 cloud 根目录下的 `package` 脚本，在 /Release/gateway 目录下找到对应的 `gateway.jar`

或者单独使用如下命令将代码打包成一个 fat jar:

```bash
mvn clean package
```

### 1.2 启动
依赖应用，需在 `gateway` 启动前启动:

| 应用 | 版本 |
|--------|--------|
|kafka|2.0.1+|
|redis|2.8+|
|注册中心eureka|1.0-SNAPSHOT|
|配置中心config|1.0-SNAPSHOT|
|uap平台|1.0.0+|

使用以下命令启动编译后的 gateway:

```bash
java -Dserver.port=8086 \
-Dlog4j.configurationFile=http://localhost:8888/*/dev/*/log4j.yml \
-Deureka.client.serviceUrl.defaultZone=http://eureka:8000/eureka \
-jar mdc-bootstrap.jar
```

可使用的参数:

| 参数 | 作用 | 默认值 |
|---|---|---|
|`-Dlog4j.configurationFile`| 指定log4j2配置文件，可使用本地文件，也可使用配置中心的文件，推荐使用配置中心文件 | http://localhost:8888/*/dev/*/log4j.yml |
|`-Deureka.client.serviceUrl.defaultZone`| 指定全局的eureka注册中心地址，使用任意eureka服务节点都可以，也可以使用`,`分隔多个地址 |http://eureka:8000/eureka|
|`-Dserver.port`| 指定gateway的端口| 8086|

其他配置项请参考 [core 组件说明文档](/common/core) 和 [Spring-cloud-gateway](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.1.3.RELEASE/)

## 2. 开发与扩展参考
### 2.1 gateway 的职责范围
`gateway` 负责所有的对外服务，即整个`mdc-cloud` 所有的对外服务都需要由 `gateway` 提供。

任何针对外部请求的鉴权、格式转换、断言、过滤、拦截、重定向、负载均衡等，理论上都应该由 `gateway` 提供。

### 2.2 定制 GlobalFilter
全局的过滤器对所有请求生效，gateway 提供了定制化的方法，需要实现 `GlobalFilter` 接口，并使用 `@Order` 注解或者实现 `Ordered` 接口中的 `getOrder()` 方法。

具体可参考 `com.cheetah.gateway.filter` 中的代码，或者 `spring-cloud-gateway`官方文档 [Global Filters](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.1.3.RELEASE/multi/multi__global_filters.html) 