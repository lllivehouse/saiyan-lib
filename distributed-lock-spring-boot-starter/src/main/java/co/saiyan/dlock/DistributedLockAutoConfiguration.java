package co.saiyan.dlock;

import co.saiyan.common.redis.annonation.EnableCommonRedis;
import co.saiyan.dlock.config.DistributedLockProperties;
import co.saiyan.dlock.registry.JdbcLockRegistry;
import co.saiyan.dlock.registry.LockRegistry;
import co.saiyan.dlock.registry.RedisLockRegistry;
import co.saiyan.dlock.repository.DefaultLockRepository;
import co.saiyan.dlock.repository.LockRepository;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = "lockRepository")
    @ConditionalOnProperty(prefix = "dlock", name = "by", havingValue = "jdbc")
    public LockRepository lockRepository(@Qualifier("dlockDataSource") DataSource dlockDataSource) {
        return new DefaultLockRepository(dlockDataSource);
    }

    @Bean(name = "lockRegistry")
    @ConditionalOnProperty(prefix = "dlock", name = "by", havingValue = "jdbc")
    public LockRegistry lockRegistry(@Qualifier("lockRepository") LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean(name = "redisLockRegistry")
    public RedisLockRegistry redisLockRegistry(@Qualifier("redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory, DistributedLockProperties distributedLockProperties) {
        return distributedLockProperties.getExpiredMs() != null ?
                new RedisLockRegistry(redisConnectionFactory, distributedLockProperties.getRedisRegistryKey(), distributedLockProperties.getExpiredMs()) :
                new RedisLockRegistry(redisConnectionFactory, distributedLockProperties.getRedisRegistryKey());
    }
}
