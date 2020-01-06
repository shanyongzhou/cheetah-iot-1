# MDC 引导程序
## 0. 概述
初始化 MDC 系统所需配置和缓存。取代原8.0 `StartupListener`。

## 1. 编译和启动
### 1.1 编译
使用 cloud 根目录下的 `package` 脚本，在 /Release/bootstrap 目录下找到对应的 `mdc-bootstrap.jar`

或者单独使用如下命令将代码打包成一个 fat jar:

```bash
mvn clean package
```

### 1.2 启动
依赖应用，需在 `mdcbootstrap` 启动前启动:

| 应用 | 版本 |
|--------|--------|
|kafka|2.0.1+|
|redis|2.8+|
|注册中心eureka|1.0-SNAPSHOT|
|配置中心config|1.0-SNAPSHOT|

使用以下命令启动编译后的 mdc-bootstrap:

```bash
java -Dserver.port=8085 \
-Dlog4j.configurationFile=http://localhost:8888/*/dev/*/log4j.yml \
-Deureka.client.serviceUrl.defaultZone=http://eureka:8000/eureka \
-jar mdc-bootstrap.jar
```

可使用的参数:

| 参数 | 作用 | 默认值 |
|---|---|---|
|`-Dlog4j.configurationFile`| 指定log4j2配置文件，可使用本地文件，也可使用配置中心的文件，推荐使用配置中心文件 | http://localhost:8888/*/dev/*/log4j.yml |
|`-Deureka.client.serviceUrl.defaultZone`| 指定全局的eureka注册中心地址，使用任意eureka服务节点都可以，也可以使用`,`分隔多个地址 |http://eureka:8000/eureka|
|`-Dserver.port`| 指定bootstrap的端口| 8085|

其他配置项请参考 [core 组件说明文档](/common/core) 

## 2. 开发和扩展参考
### 2.1 本服务职责范围
`mdc-bootstrap` 主要负责 `缓存数据` 的初始化加载和管理。

`缓存数据` 的定义:
* 被多个服务或应用频繁使用的长期缓存数据
* 不包括单个应用或服务的 `配置` 数据，配置信息请编写配置文件并由 [config](././center/config) 配置中心统一管理