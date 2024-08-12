# 赛亚人公共能力库

## 二方库介绍


- [ ] __bom__：管理共享库依赖版本，所有其它spring boot应用都需要引入此bom作为parent
- [ ] __model__：定义公共的跨多个应用的数据模型
- [ ] __util__：通用工具类，如本地缓存、http客户端、json序列化、日期处理、本地无锁队列等
<br>

- [ ] __uid-generator-spring-boot-starter__：雪花id生成器基础组件。基于baidu开源的uid-generator封装的starter，通过添加<font color=#008000>@EnableUidGenerator</font>注解快速对接
- [ ] __common-eventbus-spring-boot-starter__：本地发布订阅事件基础组件。封装google eventbus库，通过添加<font color=#008000>@EnableCommonEventBus</font>注解快速对接
- [ ] __distributed-event-spring-boot-starter__：分布式发布订阅事件基础组件。基于nacos的发送监听机制实现数据广播，通过添加<font color=#008000>@EnableDistributedEvent</font>注解快速对接
- [ ] __distributed-lock-spring-boot-starter__：分布式锁基础组件。支持redis和jdbc的两种不同分布式锁实现，通过添加<font color=#008000>@EnableDistributedLock</font>注解快速对接
- [ ] __scheduler-plus-spring-boot-starter__：动态定时任务基础组件。通过添加<font color=#008000>@EnableSchedulerPlus</font>注解快速对接。通过注入@SchedulerPlusManager bean可在运行时动态新增修改暂停查询定时任务以及运行日志
- [ ] __http-client-spring-boot-starter__：http客户端基础组件。支持http,sse两种协议的客户端，可以对服务端open api生成客户端调用代码简化http调用逻辑，通过添加<font color=#008000>@EnablOpenApiClient</font> <font color=#008000>@EnablSseClient</font>注解快速对接
<br>

- [ ] __common-mybatisplus-spring-boot-starter__：中间件组件。封装基于mybatis-plus的starter，通过添加<font color=#008000>@EnableCommonMybatisPlus</font>注解快速对接
- [ ] __common-redis-spring-boot-starter__：中间件组件。封装redis的starter，通过添加<font color=#008000>@EnableCommonRedis</font>注解快速对接。须参考<font
  color=Blue>RedisProperties.class</font>增加nacos配置项。通过注入RedisService使用
- [ ] __common-kafka-spring-boot-starter__：中间件组件。封装基于spring boot kafka的starter，通过添加<font color=#008000>@EnableCommonKafka</font>注解快速对接，详情参考此目录下的README.md
<br>

- [ ] __common-doc-spring-boot-starter__：业务公共组件。封装swagger3.0相关配置，通过添加<font color=#008000>@EnableCommonDoc</font>注解快速对接
- [ ] __common-security-spring-boot-starter__：业务公共组件。封装基于spring-security的starter，适合后端项目基于初始的数据表user,role,permission实现rbac后端鉴权，通过添加<font color=#008000>@EnableCommonSecurity</font>注解快速对接
- [ ] __common-syslog-spring-boot-starter__：业务公共组件。基于拦截器实现后台系统的操作日志记录，通过添加<font color=#008000>@EnableCommonSyslog</font>注解快速对接
- [ ] __common-indicator-spring-boot-starter__：业务公共组件。基于拦截器实现接口调用统计，支持spel表达式，通过添加<font color=#008000>@EnableCommonIndicator</font>注解快速对接
- [ ] __common-file-upload__：业务公共组件。基于开源项目file-upload而迁移过来的组件，支持主流对象存储相关的文件上传下载
<br>

- [ ] __mybatis-generator-plugin__：maven公共插件。基于mybatis-generator官方插件进行表维度的增删改查dal层代码的自动生成，支持表格查询
<br>

## 二方库发布
```shell
mvn clean deploy -Dmaven.test.skip=true -Drevision=1.0.0-SNAPSHOT # revision为版本号不指定默认为1.0.0-SNAPSHOT 正式包版本号一次性不可覆盖
```
