# 前言

NexLM 是一个提供多个大模型集成、可灵活切换的高效平台。通过统一认证登录，实现大模型服务统一代理，让用户能够轻松接入像 ChatGPT、Qwen、DeepSeek 这样的热门大模型，同时还支持这些模型本地部署集成，满足不同的应用场景和需求。


# 系统说明

- 基于 Spring Cloud 、Spring Boot、MyBatis-Plus、MySQL、Redis、Docker、Kafka 等技术栈实现的 LM 大模型统计平台；
- 采用主流的互联网机构，同时支持微服务架构和单体架构；
- 提供对 Spring Authorization Server 生产级实践，支持多种安全授权模式
- OAuth2 的 RBAC 企业快速开发平台...（持续开发中...）



# 项目介绍

### 项目演示

- 项目仓库（GitHub）：[https://github.com/pitt1997/NexLM](https://github.com/pitt1997/NexLM)
- 项目演示地址：等待上线更新

![登录页面]()

#### 前后端分离版本




#### 后台管理系统

![后台管理系统]()



#### 代码展示

![源码结构]()


### 架构图

#### 系统架构图

![系统架构图]()


#### 业务架构图

![业务架构图]()

### 组织结构

```
backend
├── nex-auth -- 认证授权模块
├── nex-boot -- 单机部署启动模块
├── nex-common -- 系统公共模块
│	├── nexus-common-bom -- 管理项目中的依赖版本，确保各个模块的依赖一致性
│	├── nex-common-core -- 平台核心基础模块，封装了常用的工具类、全局异常处理、通用配置等功能
│	├── nex-common-dubbo -- 基于 Dubbo 进行微服务 RPC 远程调用的封装
│	├── nex-common-feign -- 封装基于 Spring Cloud OpenFeign 的 HTTP 远程调用
│	├── nex-common-flyway -- 数据库版本管理模块，基于 Flyway 实现数据库迁移
│	├── nex-common-mybatis -- 封装 MyBatis 相关的配置和通用逻辑/数据源相关配置
│	├── nex-common-redis -- 封装 Redis 相关的缓存操作，包括缓存管理、分布式锁等
│	├── nex-common-web -- 提供 Web 层的通用功能，如全局拦截器、请求参数校验、统一返回结果等
├── nex-core -- 业务核心基础模块，业务公共依赖对象 VO/DTO/DO 等
├── nex-gateway -- 网关模块，统一路由，服务发现等
├── nex-manager -- 后台管理模块代码
│   ├── nex-admin -- 后台管理启动模块
│	├── nex-user -- 用户中心
│	├── nex-resource -- 资源中心
│	├── nex-role-permission -- 角色授权中心
├── nex-ui -- 模块
├── sql -- 数据库 SQL
```

#### 环境配置说明



#### 配置文件说明



### 技术选型

后端技术栈

|         技术          | 说明                   | 官网                                                                                                 |
|:-------------------:|----------------------|----------------------------------------------------------------------------------------------------|
| Spring & SpringMVC  | Java全栈应用程序框架和WEB容器实现 | [https://spring.io/](https://spring.io/)                                                           |
|     SpringBoot      | Spring应用简化集成开发框架     | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)                   |
|       mybatis       | 数据库orm框架             | [https://mybatis.org](https://mybatis.org)                                                       |
|    MyBatis-Plus    | 数据库orm框架             | [https://baomidou.com/](https://baomidou.com/)                                                     |
| ... | ... | ... |




## 环境搭建

### 开发工具

|        工具        | 说明           | 官网                                                                                                           | 
|:----------------:|--------------|--------------------------------------------------------------------------------------------------------------|
|       IDEA       | java开发工具     | [https://www.jetbrains.com](https://www.jetbrains.com)                                                       |
|     Webstorm     | web开发工具      | [https://www.jetbrains.com/webstorm](https://www.jetbrains.com/webstorm)                                     |
|      Chrome      | 浏览器          | [https://www.google.com/intl/zh-CN/chrome](https://www.google.com/intl/zh-CN/chrome)                         |
|   ScreenToGif    | gif录屏        | [https://www.screentogif.com](https://www.screentogif.com)                                                   |
|     SniPaste     | 截图           | [https://www.snipaste.com](https://www.snipaste.com)                                                         |
|     PicPick      | 图片处理工具       | [https://picpick.app](https://picpick.app)                                                                   |
|     MarkText     | markdown编辑器  | [https://github.com/marktext/marktext](https://github.com/marktext/marktext)                                 |
|       curl       | http终端请求     | [https://curl.se](https://curl.se)                                                                           |
|     Postman      | API接口调试      | [https://www.postman.com](https://www.postman.com)                                                           |
|     draw.io      | 流程图、架构图绘制    | [https://www.diagrams.net/](https://www.diagrams.net/)                                                       |
|      Axure       | 原型图设计工具      | [https://www.axure.com](https://www.axure.com)                                                     |
|     navicat      | 数据库连接工具      | [https://www.navicat.com](https://www.navicat.com)                                                           |
|     DBeaver      | 免费开源的数据库连接工具 | [https://dbeaver.io](https://dbeaver.io)                                                                     |
|      iTerm2      | mac终端        | [https://iterm2.com](https://iterm2.com)                                                                     |
| windows terminal | win终端        | [https://learn.microsoft.com/en-us/windows/terminal/install](https://learn.microsoft.com/en-us/windows/terminal/install) |
|   SwitchHosts    | host管理       | [https://github.com/oldj/SwitchHosts/releases](https://github.com/oldj/SwitchHosts/releases)                 |


### 开发环境

|      工具       | 版本        | 下载                                                                                                                     |
|:-------------:|:----------|------------------------------------------------------------------------------------------------------------------------|
|      jdk      | 1.8+      | [https://www.oracle.com/java/technologies/downloads/#java8](https://www.oracle.com/java/technologies/downloads/#java8) |
|     maven     | 3.4+      | [https://maven.apache.org/](https://maven.apache.org/)                                                                 |
|     mysql     | 5.7+/8.0+ | [https://www.mysql.com/downloads/](https://www.mysql.com/downloads/)                                                   |
|     redis     | 5.0+      | [https://redis.io/download/](https://redis.io/download/)                                                               |
| elasticsearch | 8.0.0+    | [https://www.elastic.co/cn/downloads/elasticsearch](https://www.elastic.co/cn/downloads/elasticsearch)                 |
|     nginx     | 1.10+     | [https://nginx.org/en/download.html](https://nginx.org/en/download.html)                                               |
|   rabbitmq    | 3.10.14+  | [https://www.rabbitmq.com/news.html](https://www.rabbitmq.com/news.html)                                               |
|    ali-oss    | 3.15.1    | [https://help.aliyun.com/document_detail/31946.html](https://help.aliyun.com/document_detail/31946.html)               |
|      git      | 2.34.1    | [http://github.com/](http://github.com/)                                                                               |
|    docker     | 4.10.0+   | [https://docs.docker.com/desktop/](https://docs.docker.com/desktop/)                                                   |
| let's encrypt | https证书   | [https://letsencrypt.org/](https://letsencrypt.org/)                                                                   |



# 许可证

[Apache License 2.0](https://github.com/pitt1997/NexLM/blob/main/LICENSE)

Copyright (c) 2025 pitt1997

