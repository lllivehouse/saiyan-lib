package co.mgentertainment.common.apiclient.utils;

import co.mgentertainment.common.apiclient.auth.Credential;
import co.mgentertainment.common.apiclient.auth.RsaSigner;
import co.mgentertainment.common.apiclient.auth.RsaTokenCredential;
import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.apiclient.http.ProtocolType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2023/2/12
 * @description ApiRequestUtils
 */
public class ApiRequestUtils {

    public static Map<String, String> getSignedHeader(Credential credential) throws ClientException {
        Map<String, String> signedHeader = new HashMap<>(0);
        if (credential instanceof RsaTokenCredential) {
            String token = new RsaSigner().sign(credential);
            if (Objects.nonNull(token)) {
                signedHeader.put(credential.getTokenName(), token);
            }
        }
        return signedHeader;
    }

    public static String composeUrl(ProtocolType protocol, String hostname, String version, String module,
                                    String action, Map<String, Object> queryParameters) throws ClientException {
        if (Objects.isNull(hostname)) {
            throw new ClientException("hostname must not be null.");
        }
        StringBuilder urlBuilder = new StringBuilder();
        String findStr = "://";
        if (hostname.indexOf(findStr) == -1) {
            urlBuilder.append(protocol.toString()).append(findStr);
        }
        if (hostname.endsWith("/")) {
            hostname = hostname.substring(0, hostname.length() - 1);
        }
        urlBuilder.append(hostname)
                .append(StringUtils.isNotBlank(version) ? '/' + (version.startsWith("/") ? version.substring(1) : version) : StringUtils.EMPTY)
                .append(StringUtils.isNotBlank(module) ? '/' + module : StringUtils.EMPTY)
                .append(StringUtils.isNotBlank(action) ? '/' + action : StringUtils.EMPTY);
        if (queryParameters.isEmpty()) {
            return urlBuilder.toString();
        }
        urlBuilder.append("?");
        String query = concatQueryString(queryParameters);
        String url = urlBuilder.append(query).toString();
        return url;
    }

    private static String concatQueryString(Map<String, Object> parameters) {
        if (Objects.isNull(parameters) || parameters.isEmpty()) {
            return null;
        } else {
            return parameters.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(
                    "&"));
        }
    }
}
