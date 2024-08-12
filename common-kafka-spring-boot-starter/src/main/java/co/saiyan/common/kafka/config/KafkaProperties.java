package co.saiyan.common.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: KafkaProperties
 * @author: lex
 * @date: 2024-06-13
 **/
@Data
@ConfigurationProperties(prefix = "common.kafka")
public class KafkaProperties {

    private ProducerConfig producer = new ProducerConfig();

    private ConsumerConfig consumer = new ConsumerConfig();

    @Data
    public static class ProducerConfig {
        /**
         * kafka集群信息，多个用逗号间隔
         */
        private String bootstrapServers = "";
        /**
         * 重试次数，设置大于0的值，则客户端会将发送失败的记录重新发送
         */
        private Integer retries = 0;
        /**
         * 批量处理大小，16K
         */
        private Integer batchSize = 16384;
        /**
         * 缓冲存储大小，32M
         */
        private Long bufferMemory = 33554432L;
        /**
         * 应答级别
         * 0：不等待应答
         * 1：leader副本应答
         * all：所有副本应答
         */
        private String acks = "1";
        /**
         * 生产者空间不足时，send()被阻塞的时间
         */
        private Integer maxBlockMs = 60000;
        /**
         * 批量发送延迟毫秒数，启用该功能能有效减少生产者发送消息次数，从而提高并发量
         */
        private Integer lingerMs = 1;
        /**
         * 消息的最大大小限制,也就是说send的消息大小不能超过这个限制
         */
        private Integer maxRequestSize = 1048576;
        /**
         * 压缩消息，支持四种类型，分别为：none、lz4、gzip、snappy，默认为none
         */
        private String compressionType = "none";
    }

    @Data
    public static class ConsumerConfig {
        /**
         * 是否自动提交
         */
        private Boolean enableAutoCommit = true;

        /**
         * 消费偏移配置
         * none：如果没有为消费者找到先前的offset的值,即没有自动维护偏移量,也没有手动维护偏移量,则抛出异常
         * earliest：在各分区下有提交的offset时：从offset处开始消费；在各分区下无提交的offset时：从头开始消费
         * latest：在各分区下有提交的offset时：从offset处开始消费；在各分区下无提交的offset时：从最新的数据开始消费
         */
        private String autoOffsetReset = "latest";

        /**
         * 自动提交的频率(ms)
         */
        private Integer autoCommitIntervalMs = 0;

        /**
         * Session超时设置(ms)
         */
        private Integer sessionTimeoutMs = 30000;

        private List<ConsumerHandlerConfig> handlers = new ArrayList<>();
    }

    @Data
    public static class ConsumerHandlerConfig {
        /**
         * kafka集群信息，多个用逗号间隔
         */
        private String bootstrapServers = "";
        /**
         * 消费者组id
         */
        private String groupId = "";
        /**
         * 消费的topic
         */
        private String topic = "";
        /**
         * 消费者线程处理类路径
         */
        private String classpath = "";
        /**
         * 消费者组中线程数量
         */
        private Integer concurrency = 1;
    }
}
