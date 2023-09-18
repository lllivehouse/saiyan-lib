package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.http.HttpClientConfig;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description ClientProfile
 */
public interface ClientProfile {

    /**
     * 环境url
     *
     * @return
     */
    String getHostname();

    /**
     * api版本
     *
     * @return
     */
    String getVersion();

    /**
     * getHttpClientConfig
     *
     * @return
     */
    HttpClientConfig getHttpClientConfig();

    void setHttpClientConfig(HttpClientConfig config);
}