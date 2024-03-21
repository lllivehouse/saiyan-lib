package co.saiyan.common.apiclient.sse;

import co.saiyan.common.apiclient.OpenApiClientProperties;
import co.saiyan.common.apiclient.auth.Credential;
import co.saiyan.common.apiclient.auth.RsaTokenCredential;
import co.saiyan.common.apiclient.core.ApiClient;
import co.saiyan.common.apiclient.core.DefaultApiClient;
import co.saiyan.common.apiclient.core.DefaultClientProfile;
import co.saiyan.common.apiclient.exception.ClientException;
import co.saiyan.common.utils.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author larry
 * @createTime 2023/09/18
 * @description SSE ConnectionManager
 */
@Slf4j
public class SseConnectionManager {

    private static final Map<String, ServerSentEvent> SSE_CACHE = new ConcurrentHashMap<>();

    private OpenApiClientProperties.AppMetadata appMetadata;
    private OpenApiClientProperties.ApiMetadata apiMetadata;

    public SseConnectionManager(OpenApiClientProperties.AppMetadata metadata, OpenApiClientProperties.ApiMetadata apiMetadata) {
        this.appMetadata = metadata;
        this.apiMetadata = apiMetadata;

    }

    /**
     * 服务端断线，会触发close事件自动重连
     *
     * @param retryTimes
     */
    public void connect(int retryTimes, Consumer<ServerSentMessage> callback) {
        OpenApiClientProperties.Sign sign = appMetadata.getSign();
        String clientId = sign.getIdentity();
        try {
            ServerSentEvent sse = newSSEClient(clientId).requestSse(new SseConnectRequest(apiMetadata.getModule(), apiMetadata.getAction(), "clientId", clientId),
                    new ServerSentEvent.Listener() {
                        @Override
                        public void onOpen(ServerSentEvent sse, Response response) {
                            log.info("SSE opened. Msg:{}", response.message());
                        }

                        @Override
                        public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                            log.debug("New SSE message id={} event={} message={}", id, event, message);
                            try {
                                ServerSentMessage ssm = GsonFactory.getGson().fromJson(message, ServerSentMessage.class);
                                callback.accept(ssm);
                            } catch (Throwable t) {
                                log.error("SSE on message error, message:{}", message, t);
                            }
                        }

                        @Override
                        public void onComment(ServerSentEvent sse, String comment) {
                            log.info("New SSE comment: {}", comment);
                        }

                        @Override
                        public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                            log.error("SSE sends retry time {} ms", milliseconds);
                            return true;
                        }

                        @Override
                        public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                            log.error("SSE onRetryError", throwable);
                            return false;
                        }

                        @Override
                        public void onClosed(ServerSentEvent sse) {
                            log.error("SSE connection closed");
                            int times = retryTimes;
                            try {
                                TimeUnit.SECONDS.sleep(10 * (int) Math.pow(3, times++));
                            } catch (InterruptedException ex) {
                                // ignored
                            }
                            connect(times, callback);
                        }

                        @Override
                        public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                            log.error("SSE onPreRetry");
                            throw new RuntimeException("No retry was expected");
                        }
                    });
            SSE_CACHE.put(clientId, sse);
        } catch (ClientException e) {
            log.error("SSE connect error, clientId:[{}], retryTimes:[{}]", clientId, retryTimes, e);
        }
    }

    public void disconnect(String clientId) {
        ServerSentEvent sse = SSE_CACHE.get(clientId);
        if (sse != null) {
            sse.close();
        }
    }

    private ApiClient newSSEClient(String clientId) {
        DefaultClientProfile profile = DefaultClientProfile.getProfile(appMetadata.getHost(), appMetadata.getVersion());
        // 读永不超时
        profile.getHttpClientConfig().setReadTimeoutMillis(0L);
        // 自动重连
        profile.getHttpClientConfig().setRetryOnConnectionFailure(true);
        OpenApiClientProperties.Sign sign = appMetadata.getSign();
        Credential credential = StringUtils.equalsIgnoreCase(sign.getAlgorithm(), "rsa")
                && !StringUtils.isAnyBlank(sign.getTokenName(), sign.getEncryptKey(), sign.getIdentity()) ?
                new RsaTokenCredential(sign.getTokenName(), sign.getEncryptKey(), clientId, sign.getNonce()) : null;
        return credential != null ? new DefaultApiClient(profile, credential) : new DefaultApiClient(profile);
    }
}
