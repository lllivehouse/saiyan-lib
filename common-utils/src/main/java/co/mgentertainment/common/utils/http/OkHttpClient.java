package co.mgentertainment.common.utils.http;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2023/7/1
 * @description OkHttpClient
 */
public class OkHttpClient {

    private static OkHttpClient instance;

    private okhttp3.OkHttpClient httpClient;

    private OkHttpClient() {
        init(HttpClientSettings.getDefault());
    }

    public static final OkHttpClient getInstance() {
        if (instance == null) {
            instance = new OkHttpClient();
        }
        return instance;
    }

    public String get(String url, Map<String, String> headerMap, Map<String, String> queryMap) {
        if (queryMap != null && queryMap.size() > 0) {
            String params = queryMap.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
            url += "?" + params;
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .method("GET", null);
        if (headerMap != null && headerMap.size() > 0) {
            builder.headers(Headers.of(headerMap));
        }
        Request request = builder.build();
        try (Response response = httpClient.newCall(request).execute()) {
            return new String(response.body().bytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("error to execute and get api response", e);
        }
    }

    private void init(HttpClientSettings config) {
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
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
        builder.addInterceptor(new OkhttpInterceptor(config.getMaxRetryTimes(), config.getRetryIntervalMs()));
        this.httpClient = builder.build();
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

        @Override
        public Response intercept(Chain chain) throws IOException {
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

}
