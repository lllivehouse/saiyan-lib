package co.mgentertainment.common.apiclient;

import co.mgentertainment.common.apiclient.auth.ApiToken;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description OpenApiClientProperties
 */
@Data
@ConfigurationProperties(prefix = "open-api-client")
public class OpenApiClientProperties {

    private Map<String, AppMetadata> app = new HashMap<>(0);

    @Data
    public static class AppMetadata {
        /**
         * api服务地址
         */
        private String host = StringUtils.EMPTY;

        /**
         * api服务版本,例如: web/v1,api/v1
         */
        private String version = StringUtils.EMPTY;

        /**
         * 请求方法：get,post,put,delete
         */
        private Map<String, ApiMetadata> api = new HashMap<>(0);

        /**
         * API请求动态口令
         */
        private Sign sign = new Sign();

        /**
         * API请求头固定口令
         */
        private ApiToken apiToken = new ApiToken();
    }

    @Data
    public static class Sign {
        private String tokenName;
        private String algorithm;
        private String identity;
        private String encryptKey;
        private Integer nonce;
    }

    @Data
    public static class ApiMetadata {

        /**
         * 请求方法：get,post,put,delete
         */
        private String httpMethod = StringUtils.EMPTY;

        /**
         * api模块
         */
        private String module = StringUtils.EMPTY;

        /**
         * api方法
         */
        private String action = StringUtils.EMPTY;
    }
}