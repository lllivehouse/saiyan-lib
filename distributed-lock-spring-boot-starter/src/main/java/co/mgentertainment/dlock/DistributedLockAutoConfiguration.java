package co.mgentertainment.dlock;

import co.mgentertainment.common.redis.annonation.EnableCommonRedis;
import co.mgentertainment.dlock.config.DistributedLockProperties;
import co.mgentertainment.dlock.registry.JdbcLockRegistry;
import co.mgentertainment.dlock.registry.LockRegistry;
import co.mgentertainment.dlock.registry.RedisLockRegistry;
import co.mgentertainment.dlock.repository.DefaultLockRepository;
import co.mgentertainment.dlock.repository.LockRepository;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import javax.sql.DataSource;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description DistributedLockAutoConfiguration
 */
@Configuration
@EnableCommonRedis
@EnableConfigurationProperties(value = {DistributedLockProperties.class})
public class DistributedLockAutoConfiguration {

    @Bean(name = "dlockDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dlockDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "dlock", name = "lock-type", havingValue = "jdbc", matchIfMissing = true)
    @ConditionalOnMissingBean
    public LockRepository lockRepository(@Qualifier("dlockDataSource") DataSource dlockDataSource) {
        return new DefaultLockRepository(dlockDataSource);
    }

    @Bean(name = "lockRegistry")
    @ConditionalOnProperty(prefix = "dlock", name = "lock-type", havingValue = "jdbc", matchIfMissing = true)
    @ConditionalOnMissingBean
    public LockRegistry lockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean(name = "lockRegistry")
    @ConditionalOnProperty(prefix = "dlock", name = "lock-type", havingValue = "redis")
    @ConditionalOnMissingBean
    public LockRegistry lockRegistry(@Qualifier("redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory, DistributedLockProperties distributedLockProperties) {
        return distributedLockProperties.getExpiredMs() != null ?
                new RedisLockRegistry(redisConnectionFactory, distributedLockProperties.getRedisRegistryKey(), distributedLockProperties.getExpiredMs()) :
                new RedisLockRegistry(redisConnectionFactory, distributedLockProperties.getRedisRegistryKey());
    }
}
