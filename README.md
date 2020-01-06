# MDC-cloud: 微服务化的 mdc
## 0. 概述
基于 `spring-cloud` 构建的微服务版的 MDC 系统.

## 1. 编译和使用
### 1.1 编译
* Windows
    * 一键打包
    ```bash
  call package.cmd  
  ```
    * maven 
    
    先在根目录下使用:
    ```bash
  mvn clean install
    ```
    然后到 `/center/sentinel-dashboard` 目录下执行:
    ```bash
  mvn clean package
    ```
    * docker
    //TO DO
* Linux //TO DO
    
### 1.2 启动
`mdc-cloud` 微服务依赖于以下第三方应用:

| 应用 | 版本 |
|--------|--------|
|kafka|2.0.1+|
|redis|2.8+|
|zookeeper|3.4.14+|
|uap|1.0.0+|

请在启动 `mdc-cloud` 之前确保以上对应版本的应用已正常启动
#### 1.2.1 单机快速启动
如果只是测试或者本地开发，可以使用默认配置启动，在 `/bin` 目录下提供了默认配置启动批处理脚本，需要按以下顺序运行:
1. center_single.bat
2. bootstrap_single.bat
3. gateway_single.bat

以上基础服务启动完成后，即可按照需要启动 `/app` 下具体的微服务应用。

#### 1.2.1 分布式和非测试环境
分布式环境或者生产环境请依次按照以下文档启动基础服务:
1. [eureka-server](/center/eureka)
2. [config-center](/center/config)
3. [sentinel-dashboard](/center/sentinel-dashboard)
4. [mdc-bootstrap](/bootstrap/mdc-bootstrap)
5. [gateway](/gateway)

以上基础服务启动完成后，即可按照需要启动 `/app` 下具体的微服务应用。

## 2. 基础架构和服务
### 2.1 逻辑架构图
//TO DO
### 2.2 基础服务
| 服务 | 功能简介 |是否集成 | 使用技术 |
|----- |---------|---------|---------|
|注册中心|微服务注册与发现，微服务治理的基础|是|Netflix-eureka|
|配置中心|微服务应用配置集中管理|是|Spring-cloud-config|
|权限控制|负责对外鉴权和签权|否|由`UAP`平台提供|
|消息中间件| 内部发布和订阅消息 |否|Apache Kafka|
|网关|负责处理外部请求，包括限流和负载均衡|是|Spring-cloud-gateway|
|链路追踪| |否|SkyWalking|
|流量控制|负责监控微服务各应用的请求流量|是|Alibaba sentinel|
|分布式缓存| |否|Redis|
|日志分析|负责收集、分析各服务的日志|否|ELK|
|服务容错|当服务发生大量错误时进行服务降级和限流|是|Alibaba sentinel|
