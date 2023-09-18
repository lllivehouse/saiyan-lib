package co.mgentertainment.common.apiclient.http;

import co.mgentertainment.common.apiclient.core.OkClient;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description HttpClientType
 */
public enum HttpClientType {

    OkHttp(OkClient.class),
    ;

    private Class<? extends AbstractHttpClient> implClass;

    private HttpClientType(Class<? extends AbstractHttpClient> implClass) {
        this.implClass = implClass;
    }

    public Class<? extends AbstractHttpClient> getImplClass() {
        return this.implClass;
    }
}
