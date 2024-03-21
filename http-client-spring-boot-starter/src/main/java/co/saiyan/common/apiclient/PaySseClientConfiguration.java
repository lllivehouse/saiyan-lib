package co.saiyan.common.apiclient;

import co.saiyan.common.apiclient.sse.SseConnectionManager;
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
@ConditionalOnExpression("!'${open-api-client.app.payment}'.isEmpty()")
public class PaySseClientConfiguration {

    @Bean(name = "paySseConnectionManager")
    public SseConnectionManager paySseConnectionManager(OpenApiClientProperties properties) {
        OpenApiClientProperties.AppMetadata appMetadata = properties.getApp().get(OpenApiClientConfiguration.ApplicationName.PAYMENT.getValue());
        OpenApiClientProperties.ApiMetadata apiMetadata = appMetadata.getApi().get("sse");
        return new SseConnectionManager(appMetadata, apiMetadata);
    }
}