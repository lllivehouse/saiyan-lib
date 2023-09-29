package co.mgentertainment.common.indicator.config;

import co.mgentertainment.common.indicator.IndicatorCollector;
import co.mgentertainment.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author larry
 * @createTime 2023/9/30
 * @description IndicatorAutoConfiguration
 */
@Configurable
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(name = "spring.redis")
@RequiredArgsConstructor
public class IndicatorAutoConfiguration {

    @Bean(name = "indicatorCollector")
    public IndicatorCollector indicatorCollector(RedisService redisService) {
        return new IndicatorCollector(redisService);
    }
}
