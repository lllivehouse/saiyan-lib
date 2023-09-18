package co.mgentertainment.common.apiclient.http;

import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.utils.ParameterHelper;

import javax.net.ssl.KeyManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description HttpConfig
 */
public abstract class HttpConfig {
    protected FormatType httpContentType = null;
    protected byte[] httpContent = null;
    protected MultipartFile[] multipartFiles = null;
    protected String encoding = null;
    protected Map<String, String> headers = new HashMap<>();
    protected Integer connectTimeout = null;
    protected Integer readTimeout = null;
    private String url = null;
    private MethodType method = null;
    protected boolean ignoreSSLCerts = false;
    private KeyManager[] keyManagers = null;
    private X509TrustManager[] x509TrustManagers = null;

    public KeyManager[] getKeyManagers() {
        return this.keyManagers;
    }

    public void setKeyManagers(KeyManager[] keyManagers) {
        this.keyManagers = keyManagers;
    }

    public X509TrustManager[] getX509TrustManagers() {
        return this.x509TrustManagers;
    }

    public void setX509TrustManagers(X509TrustManager[] x509TrustManagers) {
        this.x509TrustManagers = x509TrustManagers;
    }

    public boolean isIgnoreSSLCerts() {
        return this.ignoreSSLCerts;
    }

    public void setIgnoreSSLCerts(boolean ignoreSSLCerts) {
        this.ignoreSSLCerts = ignoreSSLCerts;
    }

    public HttpConfig(String strUrl) {
        this.url = strUrl;
    }

    public HttpConfig() {
    }

    public String getUrl() {
        return this.url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    public MethodType getMethod() {
        return this.method;
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    public FormatType getHttpContentType() {
        return this.httpContentType;
    }

    public void setHttpContentType(FormatType httpContentType) {
        this.httpContentType = httpContentType;
        if (null == this.httpContent && null == httpContentType) {
            this.headers.remove("Content-Type");
        } else {
            this.headers.put("Content-Type", this.getContentTypeValue(this.httpContentType, this.encoding));
        }

    }

    public byte[] getHttpContent() {
        return this.httpContent;
    }

    public void setHttpContent(byte[] content, String encoding, FormatType format) {
        if (null == content) {
            this.headers.remove("Content-MD5");
            this.headers.put("Content-Length", "0");
            this.headers.remove("Content-Type");
            this.httpContentType = null;
            this.httpContent = null;
            this.encoding = null;
        } else {
            if (this.getMethod() != null && (this.getMethod() == MethodType.GET || this.getMethod() == MethodType.HEAD)) {
                content = new byte[0];
            }

            this.httpContent = content;
            this.encoding = encoding;
            String contentLen = String.valueOf(content.length);
            String strMd5 = ParameterHelper.md5Sum(content);
            this.headers.put("Content-MD5", strMd5);
            this.headers.put("Content-Length", contentLen);
            if (null != format) {
                this.headers.put("Content-Type", FormatType.mapFormatToAccept(format));
            }

        }
    }

    public MultipartFile[] getMultipartFiles() {
        return this.multipartFiles;
    }

    public void setMultipartFiles(Map<String, File> fileMap) {
        if (null == fileMap || fileMap.isEmpty()) {
            this.multipartFiles = null;
        } else {
            List<MultipartFile> multipartFiles = fileMap.entrySet().stream().map(e -> new MultipartFile(e.getKey(),
                    e.getValue())).collect(Collectors.toList());
            this.multipartFiles = multipartFiles.toArray(new MultipartFile[]{});
        }
    }

    public void putHeaderParameter(String name, String value) {
        if (null != name && null != value) {
            this.headers.put(name, value);
        }
    }

    public String getHeaderValue(String name) {
        return this.headers.get(name);
    }

    public String getContentTypeValue(FormatType contentType, String encoding) {
        if (null != contentType && null != encoding) {
            return FormatType.mapFormatToAccept(contentType) + ";charset=" + encoding.toLowerCase();
        } else {
            return null != contentType ? FormatType.mapFormatToAccept(contentType) : null;
        }
    }

    public String getHttpContentString() throws ClientException {
        String stringContent = "";
        if (Objects.nonNull(this.httpContent)) {
            try {
                if (this.encoding == null) {
                    stringContent = new String(this.httpContent);
                } else {
                    stringContent = new String(this.httpContent, this.encoding);
                }
            } catch (UnsupportedEncodingException var3) {
                throw new ClientException("UnsupportedEncoding", "Can not parse response due to unsupported " +
                        "encoding.");
            }
        }
        return stringContent;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    @Override
    public String toString() {
        return "HttpConfig(httpContentType=" + this.getHttpContentType() + ", httpContent=" + Arrays.toString(this.getHttpContent()) + (", multipartFiles=" + this.multipartFiles != null ? Arrays.asList(this.multipartFiles).stream().map(mf -> mf.getKey() + ':' + mf.getFile().getName()).collect(Collectors.joining()) : "null") + ", encoding=" + this.getEncoding() + ", headers=" + this.getHeaders() + ", connectTimeout=" + this.getConnectTimeout() + ", readTimeout=" + this.getReadTimeout() + ", url=" + this.getUrl() + ", method=" + this.getMethod() + ", ignoreSSLCerts=" + this.isIgnoreSSLCerts() + ", keyManagers=" + Arrays.deepToString(this.getKeyManagers()) + ", x509TrustManagers=" + Arrays.deepToString(this.getX509TrustManagers()) + ")";
    }
}
