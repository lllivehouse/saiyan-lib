package co.mgentertainment.common.apiclient.core;

import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.http.*;
import co.mgentertainment.common.apiclient.sse.OkSse;
import co.mgentertainment.common.apiclient.sse.ServerSentEvent;
import co.mgentertainment.common.utils.GsonFactory;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description OkClient
 */
public class OkClient extends AbstractHttpClient {

    private okhttp3.OkHttpClient httpClient;

    private ExecutorService executorService;

    private Mimetypes mimetypes = Mimetypes.getInstance();

    @Override
    public void init(HttpClientConfig clientConfig) {
        final HttpClientConfig config = clientConfig != null ? clientConfig : HttpClientConfig.getDefault();
        this.clientConfig = config;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                // 默认信任所有证书
                .sslSocketFactory(createSSLSocketFactory(), getX509TrustManager())
                .hostnameVerifier(getHostnameVerifier())
                .connectTimeout(config.getConnectionTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
                // 默认不自动重连
                .retryOnConnectionFailure(config.isRetryOnConnectionFailure())
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections(),
                        config.getKeepAliveDurationMillis(), TimeUnit.MILLISECONDS));
        // 设置重试
        if (config.isAutoRetry() && config.getMaxRetryTimes() > 0) {
            builder.addInterceptor(new OkhttpInterceptor(config.getMaxRetryTimes(), config.getRetryIntervalMs()));
        }
        this.initExecutor();
        this.httpClient = builder.build();
    }

    @Override
    public HttpResponse syncInvoke(HttpRequest httpRequest) throws IOException, ClientException {
        HttpResponse httpResponse;
        Request request = this.parseToRequest(httpRequest);
        try (Response response = httpClient.newCall(request).execute()) {
            httpResponse = this.parseToHttpResponse(response);
            return httpResponse;
        } catch (Exception e) {
            throw new ClientException("error to execute and get api response");
        }
    }

    @Override
    public CompletableFuture<HttpResponse> asyncInvoke(final HttpRequest httpRequest,
                                                       @Nullable final CallBack callBack) {
        return CompletableFuture.supplyAsync(() -> {
            HttpResponse result = null;
            try {
                result = OkClient.this.syncInvoke(httpRequest);
            } catch (Exception ex) {
                if (Objects.nonNull(callBack)) {
                    callBack.onFailure(httpRequest, ex);
                }
            }
            if (Objects.nonNull(callBack)) {
                try {
                    callBack.onResponse(httpRequest, result);
                } catch (ClientException e) {
                }
            }
            return result;
        }, this.executorService);
    }

    @Override
    public ServerSentEvent requestSse(HttpRequest httpRequest, ServerSentEvent.Listener listener) throws ClientException {
        Request request = this.parseToRequest(httpRequest);
        return new OkSse(this.httpClient).newServerSentEvent(request, listener);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void close() {
        this.executorService.shutdown();
        this.httpClient = null;
    }

    private void initExecutor() {
        if (Objects.isNull(this.clientConfig.getExecutorService())) {
            this.executorService = new ThreadPoolExecutor(0, this.clientConfig.getMaxRequests(), 60L,
                    TimeUnit.SECONDS, new SynchronousQueue(), new ThreadFactory() {
                private AtomicInteger counter = new AtomicInteger(0);

                @Override
                public Thread newThread(@NotNull Runnable runnable) {
                    return new Thread(runnable,
                            "AIxTest_Async_API_Client_ThreadPool_" + this.counter.incrementAndGet());
                }
            });
        } else {
            this.executorService = this.clientConfig.getExecutorService();
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TrustManager[] getTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    private static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }

    private static X509TrustManager getX509TrustManager() {
        X509TrustManager trustManager = null;
        try {
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trustManager;
    }

    private static class OkhttpInterceptor implements Interceptor {
        // 最大重试次数
        private int maxRetry;
        // 重试间隔时间：毫秒
        private int retryIntervalMs;

        public OkhttpInterceptor(int maxRetry, int retryIntervalMs) {
            this.maxRetry = maxRetry;
            this.retryIntervalMs = retryIntervalMs;
        }

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            // 递归下发请求，如果仍然失败 则返回 null ,但是 intercept must not return null. 返回 null 会报 IllegalStateException 异常
            return retry(chain, 0);
        }

        Response retry(Chain chain, int retryCent) {
            Request request = chain.request();
            Response response = null;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                if (maxRetry > retryCent) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(retryIntervalMs);
                    } catch (InterruptedException ex) {
                    }
                    return retry(chain, retryCent + 1);
                }
            } finally {
                return response;
            }
        }
    }

    private Request parseToRequest(HttpRequest httpRequest) throws ClientException {
        if (Objects.isNull(httpRequest.getUrl())) {
            throw new ClientException("request url is null");
        }
        byte[] httpContent = httpRequest.getHttpContent();
        boolean hasHttpContent = Objects.nonNull(httpContent) && httpContent.length > 0;
        FormatType formatType = httpRequest.getHttpContentType();
        return new Request.Builder()
                .url(httpRequest.getUrl())
                .headers(Headers.of(httpRequest.getHeaders()))
                .method(httpRequest.getMethod().name(), FormatType.RAW == formatType ?
                        resolveRawTypeRequestBody(httpRequest) :
                        hasHttpContent ? RequestBody.create(httpContent,
                                MediaType.parse(FormatType.mapFormatToAccept(httpRequest.getHttpContentType()))) :
                                MethodType.GET.equals(httpRequest.getMethod()) ? null : RequestBody.create(null, new byte[0]))
                .build();
    }

    private RequestBody resolveRawTypeRequestBody(HttpRequest httpRequest) throws ClientException {
        MultipartFile[] multipartFiles = httpRequest.getMultipartFiles();
        String contentString = httpRequest.getHttpContentString();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (Objects.nonNull(multipartFiles) && multipartFiles.length > 0) {
            for (MultipartFile mf : multipartFiles) {
                String key = mf.getKey();
                File file = mf.getFile();
                if (Objects.nonNull(key) && Objects.nonNull(file)) {
                    String mimetype = mimetypes.getMimetype(file);
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(file, MediaType.parse(mimetype)));
                }
            }
        }
        if (Objects.nonNull(contentString)) {
            Map map = GsonFactory.getGson().fromJson(contentString, Map.class);
            if (map != null && !map.isEmpty()) {
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry e = (Map.Entry) iterator.next();
                    builder.addFormDataPart(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
                }
            }
        }
        return builder.build();
    }

    private HttpResponse parseToHttpResponse(Response response) throws IOException {
        HttpResponse result = new HttpResponse();
        result.setStatus(response.code());
        for (String headerName : response.headers().names()) {
            result.putHeaderParameter(headerName, response.header(headerName));
        }
        ResponseBody body = response.body();
        MediaType mediaType = body.contentType();
        FormatType formatType = FormatType.mapAcceptToFormat(mediaType.toString());
        result.setHttpContent(body.bytes(), StandardCharsets.UTF_8.name(), formatType);
        return result;
    }
}
