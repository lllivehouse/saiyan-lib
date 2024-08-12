# 后端服务接入kafka指南

## 方式一：
### 基于spring-boot-kafka封装为spring boot starter。同时自定义消息接受者的handler，为了方便直观的看到所有消息订阅bean，会针对每个topic消费处理bean在nacos进行配置化，基于@KafkaListener的注册机制在容器初始化时进行扫描和注册

### 接入步骤：

### 1.jar包引入，在服务所在module引入：
```xml
<dependency>
    <groupId>com.slid</groupId>
    <artifactId>common-kafka-spring-boot-starter</artifactId>
</dependency>
```
### 2.在应用启动类加上注解:@EnableCommonKafka

### 3.实现MessageHandler接口。新建topic消息订阅处理类，定义为@Component
```java
package co.saiyan.demo.service.mq;
 
import com.ac.common.utils.DateUtils;
import com.slid.live.slot.common.mq.core.MessageHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
 
import java.util.Date;
import java.util.List;
 
@Component
public class TestConsumer implements MessageHandler {
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> list) {
        list.stream().forEach(record -> {
            System.out.println(DateUtils.format(new Date()) + " TestConsumer.onMessage: " + record.value() + " " + record.topic() + " " + record.partition());
        });
    }
}
```

### 3.4.nacos配置kafka公共配置项（shared-kafka-config）添加如下：
```properties
# kafka配置开关
common.kafka.enable=true
# kafka server,集群节点逗号分隔
common.kafka.producer.bootstrapServers=172.22.5.23:9092,172.22.5.24:9092,172.22.5.25:9092
# 生产者配置（不需要发送可以不配置）
# 重试次数，0为不启用重试机制
common.kafka.producer.retries=3
# 控制批处理大小16K，单位为字节
common.kafka.producer.batchSize=16384
# 缓冲存储大小32M，生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
common.kafka.producer.bufferMemory=33554432
# 应答级别，0：不等待应答，1：leader副本应答，all：所有副本应答
common.kafka.producer.acks=1
# 生产者空间不足时，send()被阻塞的时间ms
common.kafka.producer.maxBlockMs=60000
# 批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
common.kafka.producer.lingerMs=1
# 消息的最大大小限制,也就是说send的消息大小不能超过这个限制, 默认1048576(1MB)
common.kafka.producer.maxRequestSize=1048576
# 压缩消息，支持四种类型，分别为：none、lz4、gzip、snappy，默认为none。消费者默认支持解压，所以压缩设置在生产者，消费者无需设置
common.kafka.producer.compressionType=none
# 消费者配置
# 是否自动提交offset偏移量(默认true)
common.kafka.consumer.enableAutoCommit=true
# offset偏移量规则设置
## earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
## latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
## none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
common.kafka.consumer.autoOffsetReset=earliest
# 自动提交的频率(ms)
common.kafka.consumer.autoCommitIntervalMs=100
# Session超时设置(ms)
common.kafka.consumer.sessionTimeoutMs=30000
```

### 5.应用的bootstrap.yml的spring.cloud.nacos.config新增：
```yml
- data-id: shared-kafka-config
  group: ${NACOS_GROUP}
  refresh: true
```
### 6.应用的nacos配置中配置topic消息处理相关配置（dev环境为例）

# kafka topic消费者配置
```properties
common.kafka.consumer.handlers[0].bootstrapServers=172.22.5.23:9092,172.22.5.24:9092,172.22.5.25:9092
common.kafka.consumer.handlers[0].topic=kafka-test
common.kafka.consumer.handlers[0].groupId=default
common.kafka.consumer.handlers[0].concurrency=1
common.kafka.consumer.handlers[0].classpath=com.slid.live.slot.api.game.service.mq.TestConsumer
```

### 7.消息发送代码示例: 注入kafkaTemplate即可
```java
@Resource
KafkaTemplate<String, String> kafkaTemplate;
 
@GetMapping("/send")
public void send() {
    String msg = "hello kafka:" + System.currentTimeMillis();
    String topic = "kafka-test";
    kafkaTemplate.send(topic, msg);
    System.out.println(DateUtils.format(new Date()) + " kafka send message: " + msg + ",topic:" + topic);
}
```

## 方式二：
### 当前存量服务普遍采用spring-cloud-stream框架对接了rocketmq。为了最大化复用当前的框架并简化配置，可以复用spring-cloud-stream编程模型支持kafka的中间件binding
### 接入步骤：
### 1.jar包引入，在服务所在module引入：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

### 2.mq公共配置项（shared-mq-config）添加如下(以dev环境为例)
```properties
# 存在多个mq中间件情况下必须指定一个默认binder
spring.cloud.stream.default-binder=rocketmq
# kafka服务节点配置
spring.cloud.stream.kafka.binder.brokers=172.22.5.23:9092,172.22.5.24:9092,172.22.5.25:9092
# topic不存在时自动创建
spring.cloud.stream.kafka.binder.auto-create-topics=true
# 生产者配置
# 重试次数，0为不启用重试机制
spring.cloud.stream.kafka.binder.producer-properties.retries=3
# 应答级别，0：不等待应答，1：leader副本应答，all：所有副本应答
spring.cloud.stream.kafka.binder.producer-properties.acks=0
# 生产者空间不足时，send()被阻塞的时间
spring.cloud.stream.kafka.binder.producer-properties.max.block.ms=60000
# 控制批处理大小16K，单位为字节
spring.cloud.stream.kafka.binder.producer-properties.batch.size=16384
# 批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
spring.cloud.stream.kafka.binder.producer-properties.linger.ms=1
# 缓冲存储大小32M，生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
spring.cloud.stream.kafka.binder.producer-properties.buffer.memory=33554432
# 消息的最大大小限制,也就是说send的消息大小不能超过这个限制, 默认1048576(1MB)
spring.cloud.stream.kafka.binder.producer-properties.max.request.size=1048576
# 压缩消息，支持四种类型，分别为：none、lz4、gzip、snappy，默认为none。消费者默认支持解压，所以压缩设置在生产者，消费者无需设置
spring.cloud.stream.kafka.binder.producer-properties.compression.type=none
spring.cloud.stream.kafka.binder.producer-properties.key.serializer=org.apache.kafka.common.serialization.StringSerializer
spring.cloud.stream.kafka.binder.producer-properties.value.serializer=org.apache.kafka.common.serialization.StringSerializer
# 消费者配置
# 是否自动提交offset偏移量(默认true)
spring.cloud.stream.kafka.binder.consumer-properties.enable.auto.commit=true
# 自动提交的频率(ms)
spring.cloud.stream.kafka.binder.consumer-properties.auto.commit.interval.ms=100
# Session超时设置(ms)
spring.cloud.stream.kafka.binder.consumer-properties.session.timeout.ms=30000
# offset偏移量规则设置
## earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
## latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
## none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
spring.cloud.stream.kafka.binder.consumer-properties.auto.offset.reset=earliest
spring.cloud.stream.kafka.binder.consumer-properties.key.serializer=org.apache.kafka.common.serialization.StringSerializer
spring.cloud.stream.kafka.binder.consumer-properties.value.serializer=org.apache.kafka.common.serialization.StringSerializer
```

### 3.服务内mq生产者和消费者配置项添加如下
```properties
# kafka配置
# 生产者发送topic
spring.cloud.stream.bindings.output-demo.destination=_test
# 生产者发送内容类型
spring.cloud.stream.bindings.output-demo.content-type=application/json
# 生产者分区数
spring.cloud.stream.bindings.output-demo.producer.partition-count=1
# 消费者订阅topic
spring.cloud.stream.bindings.input-demo.destination=_test
# 消费者订阅内容类型
spring.cloud.stream.bindings.input-demo.content-type=application/json
# 消费者组，如果消息要排他则每个节点需配置同一个消费者组，如果要重复消费消息，则每个节点的消费者组名称唯一
spring.cloud.stream.bindings.input-demo.group=defaultGroup
```

### 4.生产者和消费者代码
```java
public interface CustomSink {
 
    String OUTPUT_DEMO = "output-demo";
 
    @Output(CustomSink.OUTPUT_DEMO)
    MessageChannel outputDemo();
}
 
@Service
@EnableBinding(CustomSink.class)
@Slf4j
public class CustomSendService {
    @Resource
    private CustomSink customSink;
 
    /**
     * 发送消息的方法
     *
     * @param request
     */
    public void sendMessage(MessageRequest request) {
        this.sendMessage(JSON.toJSONString(request));
    }
 
    public void sendMessage(String message) {
        try {
            customSink.outputDemo().send(MessageBuilder.withPayload(message).build());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("广播消息 -> sendMessage:{} {}", message, e.getMessage());
        }
    }
}
 
 
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
 
    @Resource
    private CustomSendService customSendService;
 
 
    @GetMapping("/send")
    public void send() {
        MessageRequest mr=new MessageRequest(MessageCmdRequest.HELLO_WORLD,"test000");
        customSendService.sendMessage(mr);
        System.out.println(DateUtils.format(new Date())+" 发送消息：" + JSONObject.toJSONString(mr));
    }
}
 
 
 
 
 
 
public interface CustomSource {
 
    String INPUT_DEMO = "input-demo";
 
    @Input(CustomSource.INPUT_DEMO)
    SubscribableChannel inputDemo();
 
}
 
 
 
@Service
@EnableBinding(CustomSource.class)
public class CustomReceiveService {
 
    /***
     * spring cloud stream里面通过 Source 接收消息
     */
    @StreamListener(value = CustomSource.INPUT_DEMO)
    public void getListener(String message){
        System.out.println(DateUtils.format(new Date())+" 收到消息：" + message);
    }
}
```
