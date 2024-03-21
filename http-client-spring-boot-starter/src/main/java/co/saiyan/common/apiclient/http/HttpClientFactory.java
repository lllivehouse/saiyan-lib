package co.saiyan.common.apiclient.http;

import co.saiyan.common.apiclient.core.ApiConstants;
import co.saiyan.common.apiclient.core.ClientProfile;
import co.saiyan.common.apiclient.core.OkClient;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @author larry
 * @createTime 2022/12/10
 * @description HttpClientFactory
 */
public class HttpClientFactory {

    public static AbstractHttpClient buildClient(ClientProfile profile) {
        try {
            HttpClientConfig clientConfig = profile.getHttpClientConfig();
            if (clientConfig == null) {
                clientConfig = HttpClientConfig.getDefault();
                profile.setHttpClientConfig(clientConfig);
            }

            String customClientClassName = System.getProperty(ApiConstants.SYSTEM_PROPERTY_HTTP_CLIENT_IMPL);

            if (Objects.isNull(customClientClassName) || customClientClassName.isEmpty()) {
                customClientClassName = clientConfig.getClientType().getImplClass().getName();
            }

            Class httpClientClass = Class.forName(customClientClassName);
            if (!AbstractHttpClient.class.isAssignableFrom(httpClientClass)) {
                throw new IllegalStateException(String.format("%s is not assignable from co.saiyan.common.apiclient.core.http.AbstractHttpClient", customClientClassName));
            } else if (OkClient.class.equals(httpClientClass)) {
                OkClient client = new OkClient();
                client.init(clientConfig);
                return client;
            } else {
                Constructor<? extends AbstractHttpClient> constructor =
                        httpClientClass.getConstructor(HttpClientConfig.class);
                return constructor.newInstance(clientConfig);
            }
        } catch (Exception var5) {
            throw new IllegalStateException("HttpClientFactory buildClient failed", var5);
        }
    }
}
