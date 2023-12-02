package co.mgentertainment.common.apiclient;

import co.mgentertainment.common.apiclient.auth.ApiToken;
import co.mgentertainment.common.apiclient.auth.Credential;
import co.mgentertainment.common.apiclient.auth.RsaTokenCredential;
import co.mgentertainment.common.apiclient.core.ApiClient;
import co.mgentertainment.common.apiclient.core.DefaultApiClient;
import co.mgentertainment.common.apiclient.core.DefaultClientProfile;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

/**
 * @author larry
 * @createTime 2023/2/14
 * @description OpenApiClientConfiguration
 */
@Configuration
@EnableConfigurationProperties(OpenApiClientProperties.class)
@ConditionalOnExpression("!T(org.springframework.util.CollectionUtils).isEmpty('${open-api-client.app}')")
public class OpenApiClientConfiguration {

    @Bean(name = "openApiClients")
    public Map<String, ApiClient> getOpenApiClients(OpenApiClientProperties properties) {
        Map<String, ApiClient> apiClientMap = Maps.newHashMap();
        properties.getApp().entrySet().stream().forEach(e -> {
            String appName = e.getKey();
            OpenApiClientProperties.AppMetadata appMetadata = e.getValue();
            ApiClient apiClient = OpenApiClientConfigUtil.newApiClient(appMetadata, false);
            apiClientMap.put(appName, apiClient);
        });
        return apiClientMap;
    }

    public static class OpenApiClientConfigUtil {
        public static ApiClient newApiClient(OpenApiClientProperties.AppMetadata appMetadata, boolean isLongConnection) {
            DefaultClientProfile profile = DefaultClientProfile.getProfile(appMetadata.getHost(), appMetadata.getVersion());
            if (isLongConnection) {
                // 读永不超时
                profile.getHttpClientConfig().setReadTimeoutMillis(0L);
                // 自动重连
                profile.getHttpClientConfig().setRetryOnConnectionFailure(true);
            }
            OpenApiClientProperties.Sign sign = appMetadata.getSign();
            ApiToken apiToken = appMetadata.getApiToken();
            ApiClient client;
            if (sign != null && !StringUtils.isAnyBlank(sign.getTokenName(), sign.getAlgorithm(), sign.getEncryptKey(), sign.getIdentity())) {
                Credential credential = StringUtils.equalsAnyIgnoreCase(sign.getAlgorithm(), "rsa") ?
                        new RsaTokenCredential(sign.getTokenName(), sign.getEncryptKey(), sign.getIdentity(), sign.getNonce())
                        : null;
                client = new DefaultApiClient(profile, credential);
            } else if (apiToken != null && !StringUtils.isAnyBlank(apiToken.getHeaderName(), apiToken.getToken())) {
                client = new DefaultApiClient(profile, apiToken);
            } else {
                client = new DefaultApiClient(profile);
            }
            return client;
        }
    }


    public enum ApplicationName {
        FILE("file"),
        CONFIG_CENTER("configcenter"),
        PAYMENT("payment"),
        DATA_CENTER("datacenter"),
        ;

        private String value;

        ApplicationName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public ApplicationName getByValue(String value) {
            return Arrays.stream(ApplicationName.values()).filter(e -> StringUtils.equals(e.getValue(), value)).findFirst().orElse(null);
        }
    }
}