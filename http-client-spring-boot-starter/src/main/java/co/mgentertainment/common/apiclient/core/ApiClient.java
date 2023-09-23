package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.http.CallBack;
import co.mgentertainment.common.apiclient.sse.ServerSentEvent;

import java.util.concurrent.CompletableFuture;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description ApiClient
 */
public interface ApiClient {

    /**
     * 同步请求服务端
     *
     * @param request
     * @param <T>
     * @return
     * @throws ClientException
     */
    <T extends ApiResponse> T call(ApiRequest<T> request) throws ClientException;

    /**
     * 异步请求服务端
     * @param request
     * @param callBack
     * @param <T>
     * @return
     * @throws ClientException
     */
    <T extends ApiResponse> CompletableFuture<T> asyncCall(ApiRequest<T> request, CallBack callBack) throws ClientException;

    /**
     * 请求服务端Server-SentEvents
     *
     * @param request
     * @param listener
     * @return
     * @throws ClientException
     */
    ServerSentEvent requestSse(SseRequest request, ServerSentEvent.Listener listener) throws ClientException;
}
