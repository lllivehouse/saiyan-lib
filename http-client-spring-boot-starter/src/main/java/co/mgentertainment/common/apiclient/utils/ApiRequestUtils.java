package co.mgentertainment.common.apiclient.utils;

import co.mgentertainment.common.apiclient.auth.Credential;
import co.mgentertainment.common.apiclient.auth.RsaTokenCredential;
import co.mgentertainment.common.apiclient.auth.Signer;
import co.mgentertainment.common.apiclient.http.ProtocolType;
import co.mgentertainment.common.apiclient.exception.ClientException;
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
        Signer signer = Signer.getSigner(credential);
        if (credential instanceof RsaTokenCredential) {
            String token = signer.sign(credential);
            if (Objects.nonNull(token)) {
                signedHeader.put(signer.getTokenName(), token);
            }
        }
        return signedHeader;
    }

    public static String composeUrl(ProtocolType protocol, String hostname, String version, String module,
                                    String action, Map<String, Object> queryParameters) throws ClientException {
        if (Objects.isNull(hostname) || Objects.isNull(version) || Objects.isNull(module) || Objects.isNull(action)) {
            throw new ClientException("hostname,version,module,action must not be null.");
        }
        StringBuilder urlBuilder = new StringBuilder();
        String findStr = "://";
        if (hostname.indexOf(findStr) == -1) {
            urlBuilder.append(protocol.toString()).append(findStr);
        }
        urlBuilder.append(hostname)
                .append(StringUtils.isNotBlank(version) ? '/' + version : StringUtils.EMPTY)
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
