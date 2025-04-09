# 项目模块说明文档
## 目录
1. [项目简介](#%E9%A1%B9%E7%9B%AE%E7%AE%80%E4%BB%8B)
2. [模块划分](#%E6%A8%A1%E5%9D%97%E5%88%92%E5%88%86)
    - [chatai-common-common](#chatai-common-common)
    - [chatai-common-datasource](#chatai-common-datasource)
    - [chatai-common-kafka](#chatai-common-kafka)
    - [chatai-common-redis](#chatai-common-redis)
    - [chatai-common-dependencies](#chatai-common-dependencies)
3. [依赖管理](#%E4%BE%9D%E8%B5%96%E7%AE%A1%E7%90%86)
4. [构建与部署](#%E6%9E%84%E5%BB%BA%E4%B8%8E%E9%83%A8%E7%BD%B2)

---

## 项目简介
该项目采用模块化架构设计，主要目的是为各个业务模块提供底层基础服务和技术支持，方便扩展和维护。通过合理的模块划分，确保各个模块职责明确、低耦合，提供稳定、高效的技术栈集成方案。

项目主要包含以下基础模块：

+ 公共工具类和通用配置模块
+ 数据源和数据库访问模块
+ 消息队列（Kafka）集成模块
+ 缓存（Redis）集成模块
+ 统一的依赖管理模块

---

## 模块划分
### 1. chatai-common-common
**职责**：  
提供项目中的通用工具类、常量定义和公共配置，供其他模块复用。

**功能**：

+ 公共工具类（如日期处理、字符串处理、文件操作等）
+ 项目中使用的全局常量定义
+ 通用的异常处理、日志工具

**目录结构**：

```plain
chatai-common-common
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.platform.base.common
│   │   │   │   ├── utils (通用工具类)
│   │   │   │   ├── constants (常量定义)
│   │   │   │   ├── exceptions (异常处理类)
│   │   │   │   └── log (日志相关工具)
│   │   ├── resources
│   │   │   ├── application.properties (公共配置)
```

---

### 2. chatai-common-datasource
**职责**：  
该模块负责管理所有与数据库访问相关的内容，包括多数据源配置、连接池管理、以及 MyBatis 框架集成。

**功能**：

+ 数据库连接池配置（如 HikariCP）
+ 多数据源管理及路由
+ MyBatis 配置与 SQL 映射
+ 动态数据源的切换

**目录结构**：

```plain
chatai-common-datasource
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.platform.base.datasource
│   │   │   │   ├── config
│   │   │   │   │   ├── DataSourceConfig.java      # 多数据源配置
│   │   │   │   │   ├── DataSourceRouting.java     # 数据源路由逻辑
│   │   │   │   │   ├── HikariPoolConfig.java      # 连接池配置
│   │   │   │   ├── mybatis
│   │   │   │   │   ├── MyBatisConfig.java         # MyBatis 相关配置
│   │   │   │   │   ├── DataPermissionInterceptor.java # 数据权限 SQL 拦截器
│   │   │   │   │   ├── mapper
│   │   │   │   │   │   ├── **/*.xml               # MyBatis SQL 映射文件
│   │   │   │   └── service
│   │   │   │       ├── DataSourceService.java     # 多数据源相关操作封装
│   ├── resources
│   │   ├── application-datasource.yml             # 多数据源配置文件

```

---

### 3. chatai-common-kafka
**职责**：  
该模块负责 Kafka 消息队列的配置与集成，提供生产者与消费者的封装及相关的工具类。

**功能**：

+ Kafka 生产者和消费者配置
+ Kafka 消息发送和消费的封装
+ Kafka 消息处理工具类

**目录结构**：

```plain
chatai-common-kafka
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.platform.base.kafka
│   │   │   │   ├── producer (Kafka 生产者封装)
│   │   │   │   ├── consumer (Kafka 消费者封装)
│   │   │   │   └── utils (Kafka 工具类)
│   │   ├── resources
│   │   │   ├── application-kafka.yml (Kafka 配置)
```

---

### 4. chatai-common-redis
**职责**：  
该模块提供 Redis 缓存的集成和操作封装，管理 Redis 的客户端连接、缓存设置和数据操作。

**功能**：

+ Redis 客户端配置
+ Redis 缓存操作封装（如存取、删除、TTL 设置）
+ Redis 工具类，支持常用缓存操作

**目录结构**：

```plain
chatai-common-redis
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.platform.base.redis
│   │   │   │   ├── config (Redis 配置)
│   │   │   │   ├── service (Redis 缓存操作封装)
│   │   │   │   └── utils (Redis 工具类)
│   │   ├── resources
│   │   │   ├── application-redis.yml (Redis 配置)
```

---

### 5. chatai-common-dependencies
**职责**：  
用于管理项目的依赖版本，统一声明所有模块所需的第三方依赖，以确保版本一致性。

**功能**：

+ 管理项目中所有第三方库的版本
+ 维护 Spring Boot、MyBatis、Kafka、Redis 等依赖的版本控制

**目录结构**：

```plain
chatai-common-dependencies
├── pom.xml (依赖管理)
```

---

## 依赖管理
所有模块的依赖版本在 `chatai-common-dependencies` 模块中进行统一管理。其他模块可以通过继承该模块的 `pom.xml`，来确保依赖的版本一致性。

---

## 构建与部署
1. **构建**：  
使用 Maven 作为构建工具，通过父项目的 `pom.xml` 文件统一管理和构建所有模块。

```plain
mvn clean install
```

2. **部署**：  
每个模块都可以独立构建和部署，具体部署方式可以根据实际的环境需求来定。可以使用 CI/CD 工具（如 Jenkins）来自动化部署过程。

---

## 结语
通过模块化设计和合理的职责划分，该项目具备良好的扩展性和维护性。每个模块都有清晰的职责，确保开发团队可以高效地进行协作和迭代开发。

如有任何疑问或改进建议，请及时反馈给开发团队！

