package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.auth.Credential;
import co.mgentertainment.common.apiclient.auth.DefaultCredentialProvider;
import co.mgentertainment.common.apiclient.deserializer.Deserializable;
import co.mgentertainment.common.apiclient.deserializer.DeserializableFactory;
import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.exception.ServerException;
import co.mgentertainment.common.apiclient.http.*;
import co.mgentertainment.common.apiclient.sse.ServerSentEvent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description DefaultApiClient
 */
public class DefaultApiClient implements ApiClient {
    private int maxRetryNumber;
    private boolean autoRetry;
    private ClientProfile clientProfile;
    private Credential credential;
    private AbstractHttpClient httpClient;

    public DefaultApiClient(ClientProfile profile) {
        this(profile, new DefaultCredentialProvider().getCredential());
    }

    public DefaultApiClient(ClientProfile profile, Credential credential) {
        this.maxRetryNumber = 3;
        this.autoRetry = true;
        this.clientProfile = profile;
        this.credential = credential;
        this.httpClient = HttpClientFactory.buildClient(this.clientProfile);
        HttpClientConfig httpClientConfig = profile.getHttpClientConfig();
        if (Objects.nonNull(httpClientConfig)) {
            httpClientConfig.setAutoRetry(this.autoRetry);
            httpClientConfig.setMaxRetryTimes(this.maxRetryNumber);
        }
    }

    public <T extends ApiResponse> T call(ApiRequest<T> request) throws ClientException {
        HttpResponse httpResponse = this.doAction(request, this.credential);
        return this.parseApiResponse(request, httpResponse);
    }

    public <T extends ApiResponse> CompletableFuture<T> asyncCall(ApiRequest<T> request, CallBack callBack) throws ClientException {
        return this.asyncDoAction(request, this.credential, callBack).thenApplyAsync((httpResponse) -> {
            try {
                return this.parseApiResponse(request, httpResponse);
            } catch (ClientException var4) {
                return null;
            }
        });
    }

    @Override
    public ServerSentEvent requestSse(SseRequest request, ServerSentEvent.Listener listener) throws ClientException {
        HttpRequest httpRequest = configHttpRequest(request, this.credential);
        return this.httpClient.requestSse(httpRequest, listener);
    }

    private <T extends ApiResponse> HttpResponse doAction(ApiRequest<T> request, Credential credential) throws ClientException {
        return this.doRealAction(request, credential, request.getAcceptFormat());
    }

    private <T extends ApiResponse> HttpResponse doRealAction(ApiRequest<T> request, Credential credential, FormatType format) throws ClientException {
        try {
            HttpRequest httpRequest = this.configHttpRequest(request, credential, format);
            HttpResponse httpResponse = this.httpClient.syncInvoke(httpRequest);
            return httpResponse;
        } catch (SocketTimeoutException var6) {
            throw new ClientException("ReadTimeout", "SocketTimeoutException has occurred on a socket read or accept.The url is " + request.getUrl(), var6);
        } catch (IOException var7) {
            throw new ClientException("ServerUnreachable", "Server unreachable: connection " + request.getUrl() + " failed", var7);
        } catch (Exception var8) {
            throw new ClientException("ClientException", "Fail to call " + request.getUrl(), var8);
        }
    }

    private <T extends ApiResponse> CompletableFuture<HttpResponse> asyncDoAction(ApiRequest<T> request, Credential credential, CallBack callBack) throws ClientException {
        return this.asyncDoRealAction(request, credential, request.getAcceptFormat(), callBack);
    }

    private <T extends ApiResponse> CompletableFuture<HttpResponse> asyncDoRealAction(ApiRequest<T> request, Credential credential, FormatType format, CallBack callBack) throws ClientException {
        HttpRequest httpRequest = this.configHttpRequest(request, credential, format);
        return this.httpClient.asyncInvoke(httpRequest, callBack);
    }

    private <T extends ApiResponse> HttpRequest configHttpRequest(ApiRequest<T> request, Credential credential,
                                                                  FormatType format) throws ClientException {
        this.doActionWithProxy(request.getProtocol(), System.getenv("HTTPS_PROXY"), System.getenv("HTTP_PROXY"));
        this.doActionWithIgnoreSSL(request, true);
        FormatType requestFormatType = request.getAcceptFormat();
        if (null != requestFormatType) {
            format = requestFormatType;
        }
        if (request.getProtocol() == null) {
            request.setProtocol(this.clientProfile.getHttpClientConfig().getProtocolType());
        }
        try {
            return request.buildRequest(this.clientProfile, credential, format);
        } catch (ClientException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw new ClientException("UnsupportedEncoding");
        }
    }

    private HttpRequest configHttpRequest(SseRequest request, Credential credential) throws ClientException {
        this.doActionWithProxy(request.getProtocol(), System.getenv("HTTPS_PROXY"), System.getenv("HTTP_PROXY"));
        request.setIgnoreSSLCerts(true);
        if (request.getProtocol() == null) {
            request.setProtocol(this.clientProfile.getHttpClientConfig().getProtocolType());
        }
        try {
            return request.buildRequest(this.clientProfile, credential);
        } catch (ClientException e) {
            throw e;
        }
    }

    private void doActionWithProxy(ProtocolType protocolType, String httpsProxy, String httpProxy) {
        HttpClientConfig config = this.clientProfile.getHttpClientConfig();
        if (protocolType == ProtocolType.HTTP && httpProxy != null) {
            config.setHttpProxy(httpProxy);
        } else if (protocolType == ProtocolType.HTTPS && httpsProxy != null) {
            config.setHttpsProxy(httpsProxy);
        }
    }

    private void doActionWithIgnoreSSL(ApiRequest request, boolean isIgnore) {
        if (isIgnore) {
            request.setIgnoreSSLCerts(true);
        }
    }

    private <T extends ApiResponse> T parseApiResponse(ApiRequest<T> request, HttpResponse httpResponse) throws ClientException {
        FormatType format = httpResponse.getHttpContentType();
        if (FormatType.JSON != format && FormatType.XML != format) {
            throw new ClientException(String.format("Server response has a bad format type: %s;\nThe original return " +
                    "is: %s;", format, httpResponse.getHttpContentString()));
        } else if (httpResponse.isSuccess()) {
            return this.readResponse(request.getResponseClass(), httpResponse, format);
        } else {
            if (500 <= httpResponse.getStatus()) {
                throw new ServerException(String.valueOf(httpResponse.getStatus()), null);
            } else {
                throw new ClientException(String.valueOf(httpResponse.getStatus()), null);
            }
        }
    }

    private <T extends ApiResponse> T readResponse(Class<T> clazz, HttpResponse httpResponse, FormatType format) throws ClientException {
        String contentStr = httpResponse.getHttpContentString();
        Deserializable deserializer = DeserializableFactory.getDeserializer(format);
        T response = deserializer.deserialize(clazz, contentStr);
        return response;
    }
}
