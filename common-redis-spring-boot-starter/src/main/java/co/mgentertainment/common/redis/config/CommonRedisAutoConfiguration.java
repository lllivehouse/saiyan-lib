package co.mgentertainment.common.redis.config;

import co.mgentertainment.common.redis.service.RedisService;
import co.mgentertainment.common.redis.service.impl.RedisServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author larry
 * @createTime 2023/8/29
 * @description CommonRedisAutoConfiguration
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor
public class CommonRedisAutoConfiguration {

    @Bean
    public BeanPostProcessor lettuceConnectionFactoryBeanProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof LettuceConnectionFactory) {
                    LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) bean;
                    lettuceConnectionFactory.setPipeliningFlushPolicy(LettuceConnection.PipeliningFlushPolicy.flushOnClose());
                    lettuceConnectionFactory.setShareNativeConnection(false);
                }
                return bean;
            }
        };
    }

    @Bean(name = "redisConnectionFactory")
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        String host = redisProperties.getHost();
        if (cluster != null) {
            return new LettuceConnectionFactory(new RedisClusterConfiguration(redisProperties.getCluster().getNodes()));
        } else if (StringUtils.isNotBlank(host)) {
            return new LettuceConnectionFactory(host, redisProperties.getPort());
        }
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        GsonRedisSerializer gsonRedisSerializer = new GsonRedisSerializer();
        redisTemplate.setValueSerializer(gsonRedisSerializer);
        redisTemplate.setHashValueSerializer(gsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name = "redisService")
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisServiceImpl(redisTemplate);
    }
}
