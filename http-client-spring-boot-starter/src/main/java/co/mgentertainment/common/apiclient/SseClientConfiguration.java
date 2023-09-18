package co.mgentertainment.common.apiclient;

import co.mgentertainment.common.apiclient.auth.Credential;
import co.mgentertainment.common.apiclient.core.ApiClient;
import co.mgentertainment.common.apiclient.core.DefaultApiClient;
import co.mgentertainment.common.apiclient.core.DefaultClientProfile;
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

    @Bean(name = "sseClient")
    public ApiClient getSseClient(OpenApiClientProperties properties) {
        OpenApiClientProperties.ApiMetadata metadata = properties.getRequestApi().get("sse");
        DefaultClientProfile profile = DefaultClientProfile.getProfile(metadata.getHost(), metadata.getVersion());
        // 读永不超时
        profile.getHttpClientConfig().setReadTimeoutMillis(0L);
        // 自动重连
        profile.getHttpClientConfig().setRetryOnConnectionFailure(true);
        Credential credential = new Credential() {
            @Override
            public String getEncryptKey() {
                return metadata.getSignAlgorithm().getEncryptKey();
            }

            @Override
            public String getIdentity() {
                return properties.getClientId();
            }
        };
        return new DefaultApiClient(profile, credential);
    }

    @Bean(name = "sseConnectionManager")
    public SseConnectionManager getSseConnectionManager(OpenApiClientProperties properties) {
        OpenApiClientProperties.ApiMetadata metadata = properties.getRequestApi().get("sse");
        return new SseConnectionManager(getSseClient(properties), metadata.getModule(), metadata.getAction(), properties.getClientId());
    }
}