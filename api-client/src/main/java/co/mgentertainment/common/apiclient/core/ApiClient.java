package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.sse.ServerSentEvent;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description ApiClient
 */
public interface ApiClient {

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
