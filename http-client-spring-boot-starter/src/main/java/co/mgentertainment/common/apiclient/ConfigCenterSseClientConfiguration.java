package co.mgentertainment.common.apiclient;

import co.mgentertainment.common.apiclient.sse.SseConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
@ConditionalOnExpression("!'${open-api-client.app.configcenter}'.isEmpty()")
public class ConfigCenterSseClientConfiguration {

    @Bean(name = "ccSseConnectionManager")
    public SseConnectionManager getSseConnectionManager(OpenApiClientProperties properties) {
        OpenApiClientProperties.AppMetadata appMetadata = properties.getApp().get(OpenApiClientConfiguration.ApplicationName.CONFIG_CENTER.getValue());
        OpenApiClientProperties.ApiMetadata apiMetadata = appMetadata.getApi().get("sse");
        return new SseConnectionManager(appMetadata, apiMetadata);
    }
}