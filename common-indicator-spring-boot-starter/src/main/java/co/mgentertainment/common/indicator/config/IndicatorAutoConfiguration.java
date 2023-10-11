package co.mgentertainment.common.indicator.config;

import co.mgentertainment.common.indicator.IndicatorCollector;
import co.mgentertainment.common.redis.service.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author larry
 * @createTime 2023/9/30
 * @description IndicatorAutoConfiguration
 */
@Configuration
public class IndicatorAutoConfiguration {

    @Bean(name = "indicatorCollector")
    public IndicatorCollector indicatorCollector(final RedisService redisService) {
        return new IndicatorCollector(redisService);
    }
}
