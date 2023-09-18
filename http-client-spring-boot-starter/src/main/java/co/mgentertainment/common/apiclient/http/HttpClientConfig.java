package co.mgentertainment.common.apiclient.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description HttpClientConfig
 */
public class HttpClientConfig {

    public static final long DEFAULT_CONNECTION_TIMEOUT = 5000L;
    public static final long DEFAULT_READ_TIMEOUT = 10000L;
    public static final long DEFAULT_WRITE_TIMEOUT = 15000L;

    private HttpClientType clientType = null;
    private String customClientClassName = null;
    private boolean autoRetry;
    private boolean retryOnConnectionFailure;
    private int maxRetryTimes;
    private int retryIntervalMs;
    private int maxIdleConnections = 5;
    private long maxIdleTimeMillis = 60000L;
    private long keepAliveDurationMillis = 5000L;
    private long connectionTimeoutMillis = DEFAULT_CONNECTION_TIMEOUT;
    private long readTimeoutMillis = DEFAULT_READ_TIMEOUT;

    private long writeTimeoutMillis = DEFAULT_WRITE_TIMEOUT;
    private ProtocolType protocolType;
    private boolean ignoreSSLCerts;
    private KeyManager[] keyManagers;
    private X509TrustManager[] x509TrustManagers;
    private SecureRandom secureRandom;
    private HostnameVerifier hostnameVerifier;
    private int maxRequests;
    private int maxRequestsPerHost;
    private Runnable idleCallback;
    private ExecutorService executorService;
    private String httpProxy;
    private String httpsProxy;
    private String noProxy;
    private Map<String, Object> extParams;

    public HttpClientConfig() {
        this.protocolType = ProtocolType.HTTP;
        this.ignoreSSLCerts = true;
        this.keyManagers = null;
        this.x509TrustManagers = null;
        this.secureRandom = null;
        this.hostnameVerifier = null;
        this.maxRequests = 64;
        this.maxRequestsPerHost = 5;
        this.idleCallback = null;
        this.executorService = null;
        this.httpProxy = null;
        this.httpsProxy = null;
        this.noProxy = null;
        this.extParams = new HashMap();
        this.autoRetry = true;
        this.retryOnConnectionFailure = false;
        // 最多重试5次
        this.maxRetryTimes = 5;
        // 重试间隔1秒
        this.retryIntervalMs = 1000;
        // 最大连接数200
        this.maxIdleConnections = 200;
        // 存活时间5分钟
        this.keepAliveDurationMillis = 300000L;
    }

    public static HttpClientConfig getDefault() {
        HttpClientConfig config = new HttpClientConfig();
        config.setClientType(HttpClientType.OkHttp);
        return config;
    }

    public HttpClientType getClientType() {
        return clientType;
    }

    public void setClientType(HttpClientType clientType) {
        this.clientType = clientType;
    }

    public String getCustomClientClassName() {
        return customClientClassName;
    }

    public void setCustomClientClassName(String customClientClassName) {
        this.customClientClassName = customClientClassName;
    }

    public boolean isAutoRetry() {
        return autoRetry;
    }

    public void setAutoRetry(boolean autoRetry) {
        this.autoRetry = autoRetry;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public int getRetryIntervalMs() {
        return retryIntervalMs;
    }

    public void setRetryIntervalMs(int retryIntervalMs) {
        this.retryIntervalMs = retryIntervalMs;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public long getMaxIdleTimeMillis() {
        return maxIdleTimeMillis;
    }

    public void setMaxIdleTimeMillis(long maxIdleTimeMillis) {
        this.maxIdleTimeMillis = maxIdleTimeMillis;
    }

    public long getKeepAliveDurationMillis() {
        return keepAliveDurationMillis;
    }

    public void setKeepAliveDurationMillis(long keepAliveDurationMillis) {
        this.keepAliveDurationMillis = keepAliveDurationMillis;
    }

    public long getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(long connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public long getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(long readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public long getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    public void setWriteTimeoutMillis(long writeTimeoutMillis) {
        this.writeTimeoutMillis = writeTimeoutMillis;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public boolean isIgnoreSSLCerts() {
        return ignoreSSLCerts;
    }

    public void setIgnoreSSLCerts(boolean ignoreSSLCerts) {
        this.ignoreSSLCerts = ignoreSSLCerts;
    }

    public KeyManager[] getKeyManagers() {
        return keyManagers;
    }

    public void setKeyManagers(KeyManager[] keyManagers) {
        this.keyManagers = keyManagers;
    }

    public X509TrustManager[] getX509TrustManagers() {
        return x509TrustManagers;
    }

    public void setX509TrustManagers(X509TrustManager[] x509TrustManagers) {
        this.x509TrustManagers = x509TrustManagers;
    }

    public SecureRandom getSecureRandom() {
        return secureRandom;
    }

    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public int getMaxRequestsPerHost() {
        return maxRequestsPerHost;
    }

    public void setMaxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
    }

    public Runnable getIdleCallback() {
        return idleCallback;
    }

    public void setIdleCallback(Runnable idleCallback) {
        this.idleCallback = idleCallback;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public String getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(String httpProxy) {
        this.httpProxy = httpProxy;
    }

    public String getHttpsProxy() {
        return httpsProxy;
    }

    public void setHttpsProxy(String httpsProxy) {
        this.httpsProxy = httpsProxy;
    }

    public String getNoProxy() {
        return noProxy;
    }

    public void setNoProxy(String noProxy) {
        this.noProxy = noProxy;
    }

    public Map<String, Object> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, Object> extParams) {
        this.extParams = extParams;
    }
}
