# Eureka注册中心-中文文档
---
## 0. 概述

mdc-cloud 微服务注册中心，基于 `spring-cloud-netflix-eureka`构建的注册中心，提供服务注册和发现服务。

## 1. 编译和启动
### 1.1 编译
使用 cloud 根目录下的 `package` 脚本，在 /Release/center 目录下找到对应的 `eureka-server.jar`

或者单独使用如下命令将代码打包成一个 fat jar:

```bash
mvn clean package
```

### 1.2 启动
依赖应用:无

正常情况下，无论是单机模式还是集群模式，`注册中心` 都应该是首先启动的服务。

---
#### **单机模式**
除开
`server.port`

外不用做任何配置，默认设置就是单机模式

---
#### **集群模式**
将注册中心本身注册到eureka
```yaml
eureka:  
  client:
    register-with-eureka: true
```

开启服务发现，用于从eureka拉取已注册的服务:
```yaml
eureka:  
  client:
    fetch-registry: true
```

同时添加其他注册中心节点的服务url:

    eureka:  
      client: http://eurekaa:8001/eureka,http://eurekab:8002/eureka
      
 *实际只用添加一个处于集群中的其他节点，eureka的服务发现是双向的，只要与其中一个节点连接就会被加入到集群*

