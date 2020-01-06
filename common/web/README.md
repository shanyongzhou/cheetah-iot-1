# web 公共组件
## 0. 概述
web 公共组件主要用于处理内部微服务的网络通信，理论上所有使用spring-mvc作为框架开发的微服务应用都需要引用此依赖。

目前包含以下内容：

* 内部网络通信使用的实体类
* spring-mvc controller 层统一的advice
* 统一的 controller 抽象基类

## 1. 编译和使用
### 1.1 编译
目前 web 组件未发布到nexus，需要本地编译使用:
```bash
mvn clean install
```

### 1.2 使用
引入依赖

maven

```xml
<dependency>
    <groupId>com.cheetah.common</groupId>
    <artifactId>web</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
`web` 依赖于 `core` 组件，使用参数可参考 [core 说明文档](/common/core)

## 2. 扩展和开发
### 2.1 实体类
当微服务之间进行交互时使用了实体类的序列化与反序列化，请将实体类定义到 `com.cheetah.common.web.entity` 包下，使用微服务应用名称作为子包名。

### 2.2 Controller Advice
如果需要在 controller 层 (`webflux` 和 `webmvc` 均适用) 定义切面，实现拦截器之类的功能，请参考 `com.cheetah.common.web.handler.ErrorMessageResolver` 
使用 `@ControllerAdvice` 进行开发，更详细的使用方法请参考 [Controller Advice](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-ann-controller-advice)