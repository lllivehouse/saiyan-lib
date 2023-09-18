package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.http.HttpClientConfig;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description DefaultClientProfile
 */
public class DefaultClientProfile implements ClientProfile {
    private static DefaultClientProfile profile = null;
    private String hostname;
    private String version;
    private HttpClientConfig httpClientConfig = HttpClientConfig.getDefault();

    private DefaultClientProfile() {
    }

    public static synchronized DefaultClientProfile getProfile(String hostname, String version) {
        profile = new DefaultClientProfile(hostname, version);
        return profile;
    }

    public DefaultClientProfile(String hostname, String version) {
        this.hostname = hostname;
        this.version = version;
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public HttpClientConfig getHttpClientConfig() {
        return this.httpClientConfig;
    }

    @Override
    public void setHttpClientConfig(HttpClientConfig config) {
        this.httpClientConfig = config;
    }
}
