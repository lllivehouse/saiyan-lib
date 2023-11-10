package co.mgentertainment.dlock;

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
@EnableConfigurationProperties(value = {DistributedLockProperties.class})
public class DistributedLockAutoConfiguration {

    @Bean(name = "dlockDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dlockDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "dlock.lock-type", name = "jdbc", havingValue = "true")
    @ConditionalOnMissingBean
    public LockRepository lockRepository(@Qualifier("dlockDataSource") DataSource dlockDataSource) {
        return new DefaultLockRepository(dlockDataSource);
    }

    @Bean(name = "lockRegistry")
    @ConditionalOnProperty(prefix = "dlock.lock-type", name = "jdbc", havingValue = "true")
    @ConditionalOnMissingBean
    public LockRegistry lockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean(name = "lockRegistry")
    @ConditionalOnProperty(prefix = "dlock.lock-type", name = "redis", havingValue = "true")
    @ConditionalOnMissingBean
    public LockRegistry lockRegistry(@Qualifier("redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory, DistributedLockProperties distributedLockProperties) {
        return distributedLockProperties.getExpiredMs() != null ?
                new RedisLockRegistry(redisConnectionFactory, distributedLockProperties.getLockKey(), distributedLockProperties.getExpiredMs()) :
                new RedisLockRegistry(redisConnectionFactory, distributedLockProperties.getLockKey());
    }
}
