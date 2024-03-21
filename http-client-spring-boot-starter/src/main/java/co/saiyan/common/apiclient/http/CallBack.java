package co.saiyan.common.apiclient.http;

import co.saiyan.common.apiclient.exception.ClientException;

/**
 * @author larry
 * @createTime 2022/12/11
 * @description CallBack
 */
public interface CallBack {
    void onFailure(HttpRequest httpRequest, Exception ex);

    void onResponse(HttpRequest httpRequest, HttpResponse httpResponse) throws ClientException;
}