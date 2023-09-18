package co.mgentertainment.common.apiclient.sse;

import co.mgentertainment.common.apiclient.core.ApiClient;
import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.utils.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

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

    private ApiClient sseClient;
    private String module;
    private String action;
    private String clientId;

    public SseConnectionManager(ApiClient sseClient, String module, String action, String clientId) {
        this.sseClient = sseClient;
        this.module = module;
        this.action = action;
        this.clientId = clientId;
    }

    /**
     * 服务端断线，会触发close事件自动重连
     *
     * @param clientId
     * @param retryTimes
     */
    public void connect(String clientId, int retryTimes, Consumer<ServerSentMessage> callback) {
        try {
            ServerSentEvent sse = this.sseClient.requestSse(new SseConnectRequest(this.module, this.action, "clientId", clientId),
                    new ServerSentEvent.Listener() {
                        @Override
                        public void onOpen(ServerSentEvent sse, Response response) {
                            log.info("SSE opened. Msg:{}", response.message());
                        }

                        @Override
                        public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                            log.debug("New SSE message id={} event={} message={}", id, event, message);
                            ServerSentMessage ssm = GsonFactory.getGson().fromJson(message,
                                    ServerSentMessage.class);
                            callback.accept(ssm);
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
                            connect(clientId, times, callback);
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
}
