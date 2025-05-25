# ChatAI

ChatAI 是一个提供多个大模型集成、可灵活切换的高效平台。通过统一认证登录，实现大模型服务统一代理，让用户能够轻松接入像 ChatGPT、Qwen、DeepSeek 这样的热门大模型，同时还支持这些模型本地部署集成，满足不同的应用场景和需求。

## 项目介绍

- 基于 Spring Cloud 、Spring Boot、MyBatis-Plus、MySQL、Redis、Docker、Kafka 等技术栈实现的大模型统一集成平台；
- 采用主流的互联网架构，支持微服务架构和单体架构；
- 提供对 Spring Authorization Server 生产级实践，支持多种安全授权模式
- OAuth2 的 RBAC 企业快速开发平台...（持续开发中...）
- 接入 AI 大模型（如 DeepSeek、Qwen、ChatGPT 等）并且支持 AI 大模型本地部署
- AI 开发框架（Spring AI + LangChain4j）、- Spring AI 核心特性：如自定义拦截器、上下文持久化、结构化输出
- Prompt 工程和优化技巧、多模态特性
- RAG 知识库和向量数据库（持续开发中...）
- MCP 模型上下文协议和服务开发（持续开发中...）
- AI 智能体 Manus 原理和自主开发（持续开发中...）
- AI 服务化和 Serverless 部署（持续开发中...）

## 技术栈

项目以 Spring AI 开发框架实战为核心，涉及到多种主流 AI 客户端和工具库的运用。

- ⭐️ Java 21 + Spring Boot 3 框架
- ⭐️ 采用主流的互联网架构，支持微服务架构和单体架构
- ⭐️ 统一认证登录，支持多种安全授权模式
- ⭐️ 统一配置管理，支持配置管理和热加载
- ⭐️ Spring AI + LangChain4j
- ⭐️ RAG 知识库
- ⭐️ PGVector 向量数据库
- ⭐ Tool Calling 工具调用
- ⭐️ MCP 模型上下文协议
- ⭐️ ReAct Agent 智能体构建
- ⭐️ Serverless 计算服务
- ⭐️ AI 大模型开发平台百炼
- ⭐️ Cursor AI 代码生成 + MCP
- ⭐️ Ollama 大模型部署
- ⭐️ Kryo 高性能序列化
- ⭐️ Jsoup 网页抓取
- ⭐️ iText PDF 生成
- ⭐️ Knife4j 接口文档

RAG 核心特性：

![RAG](/docs/imgs/rag.png)

## 项目演示

- 项目仓库（GitHub）：[https://github.com/pitt1997/ChatAI](https://github.com/pitt1997/ChatAI)
- 项目演示地址：等待上线更新
- 项目效果展示：

大模型集成页面

![大模型页面](/docs/imgs/chats.png)

![大模型页面](/docs/imgs/models.png)

前后端分离登录

![登录页面](/docs/imgs/login2.png)

单机后端版登录

![登录页面](/docs/imgs/login.png)

### 后台管理系统

![后台管理系统]()

## 架构图

### 系统架构图

![系统架构图](/docs/imgs/arch.png)


### 业务架构图

![业务架构图]()

## 代码结构
后端结构
```
backend
├── chatai-auth -- 认证授权模块
├── chatai-agent -- Spring AI 智能体模块
├── chatai-boot -- 单机部署启动模块
├── chatai-common -- 系统公共模块
│    ├── chatai-common-bom -- 管理项目中的依赖版本，确保各个模块的依赖一致性
│    ├── chatai-common-core -- 平台核心基础模块，封装了常用的工具类、全局异常处理、通用配置等功能
│    ├── chatai-common-dubbo -- 基于 Dubbo 进行微服务 RPC 远程调用的封装
│    ├── chatai-common-feign -- 封装基于 Spring Cloud OpenFeign 的 HTTP 远程调用
│    ├── chatai-common-flyway -- 数据库版本管理模块，基于 Flyway 实现数据库迁移
│    ├── chatai-common-mybatis -- 封装 MyBatis 相关的配置和通用逻辑/数据源相关配置
│    ├── chatai-common-redis -- 封装 Redis 相关的缓存操作，包括缓存管理、分布式锁等
│    ├── chatai-common-web -- 提供 Web 层的通用功能，如全局拦截器、请求参数校验、统一返回结果等
├── chatai-core -- 业务核心基础模块，业务公共依赖对象 VO/DTO/DO 等
├── chatai-gateway -- 网关模块，统一路由，服务发现等
├── chatai-manager -- 后台管理模块代码
│    ├── chatai-admin -- 后台管理启动模块
│    ├── chatai-user -- 用户中心
│    ├── chatai-resource -- 资源中心
│    ├── chatai-role-permission -- 角色授权中心
├── chatai-ui -- 前端模块
├── sql -- 数据库 SQL
```

前端结构
```
chatai-ui
├── src
│   ├── api -- API接口
│   ├── assets -- 静态资源
│   ├── components -- 通用组件
│   ├── router -- 路由配置
│   ├── stores -- 状态管理
│   ├── styles -- 样式文件
│   ├── utils -- 工具函数
│   └── views -- 页面组件
├── public -- 公共资源
└── package.json -- 项目配置
```

### 环境配置说明
### 配置文件说明

## 项目启动
### 1.1 克隆代码
```bash
git clone https://github.com/pitt1997/ChatAI.git
```
### 1.2 配置依赖环境
需要提前安装并启动 Redis 和 MySQL 服务，确保项目能正确连接，微服务方式启动还需要 Nacos（单机版不需要）。

- 安装启动 MySQL 数据库。
- 导入项目到 IntelliJ IDEA 中。
- 运行 sql 文件夹中的 SQL 脚本，初始化数据库和表数据。
- 本地启动时，可选择单机版启动（chatai-boot），修改 src/main/resources/application.yml 中的数据库连接、Redis 配置。

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/chatai
    username: root
    password: 123456
  redis:
    sentinel: # 哨兵模式（默认不开启）
      master: # mymaster（此处注释则表示不开启哨兵）
      nodes: 127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381
    host: 127.0.0.1
    port: 6379
    database: 1 # 默认使用 0 库
    password: 123456
```

根据实际的情况进行修改数据库和 redis 的 ip, 用户名密码, 单机版启动可不需要依赖 Nacos。

### 1.3 配置大模型接口密钥
如果是对接 DeepSeek 在线 API 则需要配置密钥；如果是对接本地大模型需要修改本地大模型地址。
```yml
llm:
  clients:
    deepseek-r1:
      enabled: true
      api-url: https://api.deepseek.com/chat/completions
      api-key: sk-xxx
      model: deepseek-reasoner
    deepseek-v3:
      enabled: true
      api-url: https://api.deepseek.com/chat/completions
      api-key: sk-xxx
      model: deepseek-chat
```

### 1.4 启动项目

- 导入项目到 IntelliJ IDEA 中。
- 运行 BootApplication.java 启动 SpringBoot 项目。
- 访问大模型集成平台（账号密码：admin/123456）：
    - 登录页面地址：http://localhost:8080/web/auth/login
    - 大模型页面地址: http://localhost:8080/web/auth/chat

使用账号密码登录大模型统一认证之后会自动跳转到大模型页面。

### 1.5 启动项目（前后端分离）

- 导入项目到 IntelliJ IDEA 中。
- 后端运行 BootApplication.java 启动 SpringBoot 项目。
- 前端运行 进入 chatai-ui 目录后执行 npm run dev（注意需要基于Node环境）。
- 访问大模型集成平台（账号密码：admin/123456）：
  - 登录页面地址：http://localhost:3000/login
  - 大模型页面地址: http://localhost:3000/chat

## 技术选型

后端技术栈

|         技术         | 说明                   | 官网                                                                                                 |
|:------------------:|----------------------|----------------------------------------------------------------------------------------------------|
| Spring & SpringMVC | Java全栈应用程序框架和WEB容器实现 | [https://spring.io/](https://spring.io/)                                                           |
|     SpringBoot     | Spring应用简化集成开发框架     | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)                   |
|      MyBatis       | 数据库orm框架             | [https://mybatis.org](https://mybatis.org)                                                       |
|    MyBatis-Plus    | 数据库orm框架             | [https://baomidou.com/](https://baomidou.com/)                                                     |
|        ...         | ... | ... |


## 环境搭建

### 开发工具

|        工具        | 说明           | 官网                                                                                                           | 
|:----------------:|--------------|--------------------------------------------------------------------------------------------------------------|
|  IntelliJ IDEA   | java开发工具     | [https://www.jetbrains.com](https://www.jetbrains.com)                                                       |
|     Webstorm     | web开发工具      | [https://www.jetbrains.com/webstorm](https://www.jetbrains.com/webstorm)                                     |
|      Chrome      | 浏览器          | [https://www.google.com/intl/zh-CN/chrome](https://www.google.com/intl/zh-CN/chrome)                         |
|   ScreenToGif    | gif录屏        | [https://www.screentogif.com](https://www.screentogif.com)                                                   |
|     SniPaste     | 截图           | [https://www.snipaste.com](https://www.snipaste.com)                                                         |
|     PicPick      | 图片处理工具       | [https://picpick.app](https://picpick.app)                                                                   |
|     MarkText     | markdown编辑器  | [https://github.com/marktext/marktext](https://github.com/marktext/marktext)                                 |
|     Postman      | API接口调试      | [https://www.postman.com](https://www.postman.com)                                                           |
|     draw.io      | 流程图、架构图绘制    | [https://www.diagrams.net/](https://www.diagrams.net/)                                                       |
|      Axure       | 原型图设计工具      | [https://www.axure.com](https://www.axure.com)                                                     |
|     Navicat      | 数据库连接工具      | [https://www.navicat.com](https://www.navicat.com)                                                           |
|     DBeaver      | 免费开源的数据库连接工具 | [https://dbeaver.io](https://dbeaver.io)                                                                     |
|      iTerm2      | mac终端        | [https://iterm2.com](https://iterm2.com)                                                                     |
| Windows terminal | win终端        | [https://learn.microsoft.com/en-us/windows/terminal/install](https://learn.microsoft.com/en-us/windows/terminal/install) |
|   SwitchHosts    | host管理       | [https://github.com/oldj/SwitchHosts/releases](https://github.com/oldj/SwitchHosts/releases)                 |


### 开发环境

|   工具   | 版本      | 下载                                                                                                                     |
|:------:|:--------|------------------------------------------------------------------------------------------------------------------------|
|  JDK   | 21+     | [https://www.oracle.com/java/technologies/downloads/#java8](https://www.oracle.com/java/technologies/downloads/#java8) |
| Maven  | 3.9+    | [https://maven.apache.org/](https://maven.apache.org/)                                                                 |
| MySQL  | 8.0+    | [https://www.mysql.com/downloads/](https://www.mysql.com/downloads/)                                                   |
| Redis  | 5.0+    | [https://redis.io/download/](https://redis.io/download/)                                                               |
| Nginx  | 1.10+   | [https://nginx.org/en/download.html](https://nginx.org/en/download.html)                                               |
|  Git   | 2.39.5  | [http://github.com/](http://github.com/)                                                                               |
| Docker | 4.10.0+ | [https://docs.docker.com/desktop/](https://docs.docker.com/desktop/)                                                   |
