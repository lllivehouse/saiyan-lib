package co.saiyan.common.apiclient.http;

import co.saiyan.common.apiclient.exception.ClientException;
import co.saiyan.common.apiclient.sse.ServerSentEvent;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description AbstractHttpClient
 */
public abstract class AbstractHttpClient implements Closeable {

    protected HttpClientConfig clientConfig;

    protected AbstractHttpClient() {
    }

    protected AbstractHttpClient(HttpClientConfig clientConfig) throws ClientException {
        if (clientConfig == null) {
            clientConfig = HttpClientConfig.getDefault();
        }

        this.clientConfig = clientConfig;
        this.init(clientConfig);
    }

    protected abstract void init(HttpClientConfig config) throws ClientException;

    public abstract HttpResponse syncInvoke(HttpRequest httpRequest) throws IOException, ClientException;

    public abstract CompletableFuture<HttpResponse> asyncInvoke(HttpRequest httpRequest, @Nullable CallBack callBack);

    public abstract ServerSentEvent requestSse(HttpRequest httpRequest, ServerSentEvent.Listener listener) throws ClientException;

    public abstract boolean isSingleton();
}
