package co.mgentertainment.common.security.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description SecureProperties
 */
@Data
@ConfigurationProperties(prefix = "secure")
public class SecureProperties {

    /**
     * secret key of jwt token
     */
    private String jwtSecret = StringUtils.EMPTY;

    /**
     * access token expired second
     */
    private Long tokenTtl = 60L;

    /**
     * refresh token expired second
     */
    private Long refreshTokenTtl = 6 * 3600L;

    /**
     * public key for refresh token encryption
     */
    private String publicKey = StringUtils.EMPTY;

    /**
     * private key for refresh token decryption
     */
    private String privateKey = StringUtils.EMPTY;

    /**
     * ignored web routes to be auth
     */
    private List<String> ignoredUrls= new ArrayList<>();

}