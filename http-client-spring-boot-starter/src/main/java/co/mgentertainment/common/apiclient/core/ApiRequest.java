package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.auth.Credential;
import co.mgentertainment.common.apiclient.http.FormatType;
import co.mgentertainment.common.apiclient.http.HttpRequest;
import co.mgentertainment.common.apiclient.http.ProtocolType;
import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.utils.ApiRequestUtils;
import co.mgentertainment.common.apiclient.utils.ParameterHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description ApiRequest
 */
public abstract class ApiRequest<T extends ApiResponse> extends HttpRequest {

    private final Map<String, Object> queryParameters = new HashMap();
    private final Map<String, Object> bodyParameters = new HashMap();
    private String hostname;
    private String version;
    private String module;
    private String action;
    private FormatType acceptFormat;
    private ProtocolType protocol;

    protected ApiRequest(String module, String action) {
        super(null);
        this.module = module;
        this.action = action;
        this.setAcceptFormat(FormatType.JSON);
    }

    public abstract Class<T> getResponseClass();

    public HttpRequest buildRequest(ClientProfile profile, Credential credential, FormatType formatType) throws ClientException,
            UnsupportedEncodingException {
        if (Objects.nonNull(credential)) {
            Map<String, String> signedHeader = ApiRequestUtils.getSignedHeader(credential);
            this.headers.putAll(signedHeader);
            if (Objects.nonNull(bodyParameters) && !bodyParameters.isEmpty()) {
                byte[] data;
                if (FormatType.JSON == this.getHttpContentType()) {
                    data = ParameterHelper.getJsonData(bodyParameters);
                } else if (FormatType.XML == this.getHttpContentType()) {
                    data = ParameterHelper.getXmlData(bodyParameters);
                } else if (FormatType.FORM == this.getHttpContentType()) {
                    data = ParameterHelper.getFormData(bodyParameters);
                } else if (FormatType.RAW == this.getHttpContentType()) {
                    Map<String, File> formFiles = ParameterHelper.getFormFiles(bodyParameters);
                    this.setMultipartFiles(formFiles);
                    data = ParameterHelper.getFormData(bodyParameters);
                } else {
                    data = null;
                }
                this.setHttpContent(data, StandardCharsets.UTF_8.name(), formatType);
            }
        }
        this.setUrl(ApiRequestUtils.composeUrl(this.protocol, profile.getHostname(), profile.getVersion(), module, action, queryParameters));
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

    public Map<String, Object> getBodyParameters() {
        return Collections.unmodifiableMap(this.bodyParameters);
    }

    protected void putBodyParameter(String name, Object value) {
        this.setParameter(this.bodyParameters, name, value);
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
