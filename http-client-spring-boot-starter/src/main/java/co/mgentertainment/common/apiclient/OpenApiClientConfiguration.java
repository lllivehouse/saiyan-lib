package co.mgentertainment.common.apiclient;

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

import java.util.Map;

/**
 * @author larry
 * @createTime 2023/2/14
 * @description OpenApiClientConfiguration
 */
@Configuration
@EnableConfigurationProperties(OpenApiClientProperties.class)
@ConditionalOnExpression("!T(org.springframework.util.CollectionUtils).isEmpty('${open-api-client.api}')")
public class OpenApiClientConfiguration {

    @Bean(name = "openApiClients")
    public Map<String, ApiClient> getOpenApiClients(OpenApiClientProperties properties) {
        Map<String, ApiClient> apiClientMap = Maps.newHashMap();
        properties.getApi().entrySet().stream().forEach(e -> {
            String apiName = e.getKey();
            OpenApiClientProperties.ApiMetadata apiMetadata = e.getValue();
            DefaultClientProfile profile = DefaultClientProfile.getProfile(apiMetadata.getHost(), apiMetadata.getVersion());
            OpenApiClientProperties.SignAlgorithm signAlgorithm = apiMetadata.getSignAlgorithm();
            ApiClient client;
            if (signAlgorithm != null && !StringUtils.isAnyBlank(signAlgorithm.getName(), signAlgorithm.getEncryptKey())) {
                Credential credential = StringUtils.equalsAnyIgnoreCase(signAlgorithm.getName(), "rsa") ? new RsaTokenCredential(signAlgorithm.getEncryptKey(), signAlgorithm.getName())
                        : null;
                client = new DefaultApiClient(profile, credential);
            } else if (apiMetadata.getApiToken() != null && !StringUtils.isAnyBlank(apiMetadata.getApiToken().getHeaderName(), apiMetadata.getApiToken().getToken())) {
                client = new DefaultApiClient(profile, apiMetadata.getApiToken());
            } else {
                client = new DefaultApiClient(profile);
            }
            apiClientMap.put(apiName, client);
        });
        return apiClientMap;
    }
}