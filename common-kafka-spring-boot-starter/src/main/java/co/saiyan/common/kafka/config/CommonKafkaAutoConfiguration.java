package co.saiyan.common.kafka.config;

import cn.hutool.crypto.digest.MD5;
import co.saiyan.common.kafka.core.MessageHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.*;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import javax.naming.ConfigurationException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: KafkaAutoConfiguration
 * @author: lex
 * @date: 2024-06-13
 **/
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@ConditionalOnProperty(prefix = "common.kafka", name = "enable", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class CommonKafkaAutoConfiguration implements Ordered, SmartInitializingSingleton, ApplicationContextAware {

    // 注入 Kafka 监听器端点注册表
    private final KafkaListenerEndpointRegistry registry;

    // 注入 Kafka 监听器注解处理器
    private final KafkaListenerAnnotationBeanPostProcessor<String, String> postProcessor;

    private final KafkaProperties kafkaProperties;

    private ApplicationContext applicationContext;

    // 存储 Kafka 消费者工厂的映射，使用并发哈希映射保证线程安全
    @Getter
    private Map<String, ConsumerFactory<String, String>> consumerFactoryMap = new ConcurrentHashMap<>();

    /**
     * Producer Template 配置
     */
    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Producer 工厂配置
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {
        // 获取所有 Kafka 消费者处理器配置
        List<KafkaProperties.ConsumerHandlerConfig> consumerHandlerConfigs = kafkaProperties.getConsumer().getHandlers();
        // 获取消息处理方法工厂
        MessageHandlerMethodFactory methodFactory = postProcessor.getMessageHandlerMethodFactory();
        for (KafkaProperties.ConsumerHandlerConfig handlerConfig : consumerHandlerConfigs) {
            // 获取 Kafka server 地址
            String bootstrapServers = handlerConfig.getBootstrapServers();
            // 如果工厂映射中尚未有此 broker 的工厂，创建一个
            if (!consumerFactoryMap.containsKey(bootstrapServers)) {
                ConsumerFactory<String, String> consumerFactory = consumerFactory(bootstrapServers);
                consumerFactoryMap.put(bootstrapServers, consumerFactory);
            }
            // 从映射中获取 Kafka 消费者工厂
            ConsumerFactory<String, String> consumerFactory = consumerFactoryMap.get(bootstrapServers);

            String id = MD5.create().digestHex(bootstrapServers);

            // 创建 Kafka 监听器容器工厂
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            // 设置并发量
            factory.setConcurrency(handlerConfig.getConcurrency());

            // 创建方法级 Kafka 监听器端点
            MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
            // 通过类路径获取消息处理bean
            Object obj = applicationContext.getBean(Class.forName(handlerConfig.getClasspath()));
            if (!(obj instanceof MessageHandler)) {
                throw new ConfigurationException("Not found " + handlerConfig.getClasspath() + " which implement the MessageHandler interface");
            }
            MessageHandler handler = (MessageHandler) obj;
            // 创建类实例
            endpoint.setBean(handler);
            // 设置方法
            Method method = handler.getClass().getDeclaredMethod("onMessage", List.class);
            endpoint.setMethod(method);
            // 设置消息处理方法工厂
            endpoint.setMessageHandlerMethodFactory(methodFactory);
            // 设置端点 ID 和组 ID
            endpoint.setId("consumer-" + id);
            endpoint.setGroupId(handlerConfig.getGroupId());
            // 设置监听的主题
            endpoint.setTopics(handlerConfig.getTopic());
            // 设置端点的并发量
            endpoint.setConcurrency(handlerConfig.getConcurrency());
            // 设置为批处理监听器
            endpoint.setBatchListener(true);
            // 注册监听器容器
            registry.registerListenerContainer(endpoint, factory, false);
        }
        // 启动所有注册的监听器容器
        registry.start();
    }

    /**
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Producer 参数配置
     */
    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // 指定多个kafka集群多个地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
        // 重试次数，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
        //同步到副本, 默认为1
        // acks=0 把消息发送到kafka就认为发送成功
        // acks=1 把消息发送到kafka leader分区，并且写入磁盘就认为发送成功
        // acks=all 把消息发送到kafka leader分区，并且leader分区的副本follower对消息进行了同步就任务发送成功
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
        // 生产者空间不足时，send()被阻塞的时间，
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaProperties.getProducer().getMaxBlockMs());
        // 控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getProducer().getBatchSize());
        // 批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getProducer().getLingerMs());
        // 生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getProducer().getBufferMemory());
        // 消息的最大大小限制,也就是说send的消息大小不能超过这个限制, 默认1048576(1MB)
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkaProperties.getProducer().getMaxRequestSize());
        // 键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 压缩消息，支持四种类型，分别为：none、lz4、gzip、snappy，默认为none。
        // 消费者默认支持解压，所以压缩设置在生产者，消费者无需设置。
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProperties.getProducer().getCompressionType());
        return props;
    }

    private ConsumerFactory<String, String> consumerFactory(String bootstrapServers) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(bootstrapServers));
    }

    private Map<String, Object> consumerConfigs(String bootstrapServers) {
        Map<String, Object> propsMap = new HashMap<>();
        // Kafka地址
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        //配置默认分组，这里没有配置+在监听的地方没有设置groupId，多个服务会出现收到相同消息情况
//        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "defaultGroup");
        // 是否自动提交offset偏移量(默认true)
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getConsumer().getEnableAutoCommit());
        // 自动提交的频率(ms)
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getConsumer().getAutoCommitIntervalMs());
        // Session超时设置
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getConsumer().getSessionTimeoutMs());
        // 键的反序列化方式
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 值的反序列化方式
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // offset偏移量规则设置：
        // (1)、earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        // (2)、latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        // (3)、none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        return propsMap;
    }
}
