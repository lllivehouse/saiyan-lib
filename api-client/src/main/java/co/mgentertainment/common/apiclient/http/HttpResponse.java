package co.mgentertainment.common.apiclient.http;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description HttpResponse
 */
public class HttpResponse extends HttpConfig {
    private int status;

    public HttpResponse(String strUrl) {
        super(strUrl);
    }

    public HttpResponse() {
    }

    @Override
    public void setHttpContent(byte[] content, String encoding, FormatType format) {
        this.httpContent = content;
        this.encoding = encoding;
        this.httpContentType = format;
    }

    @Override
    public String getHeaderValue(String name) {
        String value = this.headers.get(name);
        if (null == value) {
            value = this.headers.get(name.toLowerCase());
        }
        return value;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return 200 <= this.status && this.status < 300;
    }

    @Override
    public String toString() {
        return "HttpResponse(super=" + super.toString() + ", status=" + this.getStatus() + ")";
    }
}
