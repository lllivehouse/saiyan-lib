package co.mgentertainment.common.apiclient;

import co.mgentertainment.common.apiclient.sse.SseConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author larry
 * @createTime 2023/2/14
 * @description OpenApiClientConfiguration
 */
@Configuration
@EnableConfigurationProperties(OpenApiClientProperties.class)
@ConditionalOnProperty(prefix = "open-api-client.requestApi", name = "sse", havingValue = "true")
public class SseClientConfiguration {

    @Bean(name = "sseConnectionManager")
    public SseConnectionManager getSseConnectionManager(OpenApiClientProperties properties) {
        OpenApiClientProperties.ApiMetadata metadata = properties.getRequestApi().get("sse");
        return new SseConnectionManager(metadata);
    }
}