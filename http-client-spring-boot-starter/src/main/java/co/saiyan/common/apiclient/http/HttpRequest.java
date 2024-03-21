package co.saiyan.common.apiclient.http;

import java.util.Map;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description HttpRequest
 */
public class HttpRequest extends HttpConfig {
    public HttpRequest(String requestUrl) {
        super(requestUrl);
    }

    public HttpRequest(String requestUrl, Map<String, String> headers) {
        super(requestUrl);
        if (null != headers) {
            this.headers = headers;
        }
    }

    @Override
    public String toString() {
        return "HttpRequest(super=" + super.toString() + ")";
    }
}
