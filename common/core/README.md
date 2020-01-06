# 核心公共组件
## 0. 概述

`mdc-cloud` 项目的核心公共组件，任何创建于项目下的微服务都应引用的依赖。

包含以下与具体业务无关的功能与配置:

* 消息通道和消息格式定义
* mybatis 全局配置
* 日志系统全局配置
* 缓存机制全局配置
* validation 全局配置
* 系统通用的常量、枚举

## 1. 编译与使用
### 1.1 编译
目前核心组件未发布到nexus，需要本地编译使用:
```bash
mvn clean install
```

### 1.2 使用
引入依赖

maven

```xml
<dependency>
    <groupId>com.cheetah.common</groupId>
    <artifactId>core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
---
使用core组件的配置:

`application.properties` 或 `application.yml`文件


|配置项|作用|是否必需|示例值|默认值|
|---|---|---|---|---|
|spring.redis.sentinel.master|redis哨兵节点监控的master节点名称|是|mymaster|
|spring.redis.sentinel.nodes|redis哨兵节点`host:port`列表，用`,`分隔|是|127.0.0.1:26379,172.30.0.12:26380|
|spring.redis.database|使用的redis数据库索引编号，取 0~15|否|0|0|
|spring.redis.jedis.pool.max-active|jedis客户端的连接池最大连接数量|否|4|8|
|spring.redis.jedis.pool.max-idle|jedis客户端的连接池最大空闲连接数量|否|4|8|
|spring.redis.jedis.pool.min-idle|jedis客户端的连接池最小空闲连接数量|否|1|0|
|spring.redis.jedis.pool.max-wait|jedis客户端的连接最大block等待时间，负数表示无限制|否|350ms|-1ms|
|spring.cloud.stream.bindings.cache_update_input.destination|全局缓存操作消息在kafka对应的topic|是|cheetah_cache||
|spring.cloud.stream.kafka.binder.brokers|kafka broker 服务地址列表，不加`port`默认使用`9092`端口|否|localhost:9092|localhost|
|spring.cloud.stream.kafka.binder.zkNodes|kafka 使用的 zookeeper 服务地址列表，不加`port`默认使用`2181`端口|否|localhost:2181|localhost|
|spring.cloud.sentinel.transport.dashboard|alibaba sentinel 控制台服务地址，用于全局微服务的流量控制|是|localhost:8081||
|sentinel.zookeeper.address|用于流量控制规则管理的zookeeper服务地址，可与kafka所使用的zookeeper保持一致|是|localhost:2181||

## 2. 改造和扩展
### 2.1 validation
目前 validation 支持三种 locale 的提示信息:
* zh_CN
* en_US
* fr_FR

如果需要定制更多的选择，可以参考静态工厂 `com.cheetah.common.core.validate.LocaleValidatorFactory`
的代码添加相应的 `validator` 生产代码。
同时添加相应的 `@Bean` 到 `com.cheetah.common.core.config.ValidateConfig` 中。

### 2.2 spring-cloud-stream channel
`mdc-cloud` 使用 kafka 作为消息中间件，如果有需要全局广播的消息，可以在 core 组件中定义相关的 channel。

定义好的 channel 接口放置于 `com.cheetah.common.core.channel` 包下，具体使用方式请参考 [spring-cloud-stream](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/2.2.1.RELEASE/spring-cloud-stream.html#_programming_model)

### 2.3 业务无关的公共服务接口
core 组件定义了一些与具体业务无关的公共服务接口，并提供了默认实现，如：

* 国际化资源：使用 `com.cheetah.common.core.service.LocaleCache` bean的相关功能
* 系统配置参数：使用 `com.cheetah.common.core.service.SystemConfigCache` bean的相关功能

