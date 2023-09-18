package co.mgentertainment.common.apiclient.http;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description ProtocolType
 */
public enum ProtocolType {
    HTTP("http"),
    HTTPS("https");

    private final String protocol;

    ProtocolType(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return this.protocol;
    }
}
