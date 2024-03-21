package co.saiyan.common.devent;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 分布式事件相关配置
 * Created by larry on 2022/3/4.
 */
@Configuration
@EnableConfigurationProperties(NacosDiscoveryProperties.class)
@RequiredArgsConstructor
public class DistributedEventAutoConfiguration {

    @Bean
    public DistributedEventProvider distributedEventProvider(NacosDiscoveryProperties nacosDiscoveryProperties, Environment environment) {
        return new DistributedEventProvider<>(nacosDiscoveryProperties, environment);
    }

}
