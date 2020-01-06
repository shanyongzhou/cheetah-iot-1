# config 配置中心-中文文档
## 0. 概述

配置中心基于 `spring-cloud-config` 构建，负责管理整个微服务框架下除 eureka、config、sentinel-dashboard 外所有应用的配置文件。同时基于 `spring-cloud-bus` 配合 kafka 实现配置更新推送。

目前版本只使用本地文件系统作为配置文件仓库。

## 1. 编译与启动
### 1.1 编译
使用 cloud 根目录下的 `package` 脚本，在 /Release/center 目录下找到对应的 `config-center.jar`

或者单独使用如下命令将代码打包成一个 fat jar:

```bash
mvn clean package
```

### 1.2 启动
依赖应用，需在 `config` 启动前启动:

| 应用 | 版本 |
|--------|--------|
|kafka|2.0.1+|
|配置中心eureka|与 config 相同|

使用以下命令启动编译后的配置中心:

```bash
java -Dserver.port=8888 \
-Dspring.cloud.config.server.native.searchLocations=file:///D:/configRepo \
-Deureka.client.serviceUrl.defaultZone=http://eureka:8000/eureka \
-jar config-center.jar
```

可使用的参数:

| 参数 | 作用 | 默认值 |
|---|---|---|
|`-Dspring.cloud.config.server.native.searchLocations`| 指定本地文件路径作为配置文件仓库，windows环境下使用`file:///`作为前缀，linux环境下使用`file://`作为前缀 | file:///D:/configRepo |
|`-Deureka.client.serviceUrl.defaultZone`| 指定全局的eureka注册中心地址，使用任意eureka服务节点都可以，也可以使用`,`分隔多个地址 |http://eureka:8000/eureka|
|`-Dserver.port`| 指定配置中心的端口| 8888|