package co.saiyan.common.apiclient.core;

import co.saiyan.common.apiclient.auth.Credential;
import co.saiyan.common.apiclient.exception.ClientException;
import co.saiyan.common.apiclient.http.FormatType;
import co.saiyan.common.apiclient.http.HttpRequest;
import co.saiyan.common.apiclient.http.ProtocolType;
import co.saiyan.common.apiclient.utils.ApiRequestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author larry
 * @createTime 2023/02/12
 * @description SseRequest
 */
public abstract class SseRequest extends HttpRequest {
    private final Map<String, Object> queryParameters = new HashMap();
    private String hostname;
    private String version;
    private String module;
    private String action;
    private FormatType acceptFormat;
    private ProtocolType protocol;

    protected SseRequest(String module, String action) {
        super(null);
        this.module = module;
        this.action = action;
        this.setAcceptFormat(FormatType.TEXT_EVENT_STREAM_VALUE);
    }

    public HttpRequest buildRequest(ClientProfile profile, Credential credential) throws ClientException {
        if (Objects.nonNull(credential)) {
            Map<String, String> signedHeader = ApiRequestUtils.getSignedHeader(credential);
            this.headers.putAll(signedHeader);
        }
        this.setUrl(ApiRequestUtils.composeUrl(this.protocol, profile.getHostname(), profile.getVersion(), module,
                action, queryParameters));
        return this;
    }

    public FormatType getAcceptFormat() {
        return this.acceptFormat;
    }

    public void setAcceptFormat(FormatType acceptFormat) {
        this.acceptFormat = acceptFormat;
        this.putHeaderParameter("Accept", FormatType.mapFormatToAccept(acceptFormat));
    }

    public ProtocolType getProtocol() {
        return this.protocol;
    }

    public void setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
    }

    public Map<String, Object> getQueryParameters() {
        return Collections.unmodifiableMap(this.queryParameters);
    }

    public <K> void putQueryParameter(String name, K value) {
        this.setParameter(this.queryParameters, name, value);
    }

    protected void setParameter(Map<String, Object> map, String name, Object value) {
        if (null != map && name != null && value != null) {
            map.put(name, value);
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
