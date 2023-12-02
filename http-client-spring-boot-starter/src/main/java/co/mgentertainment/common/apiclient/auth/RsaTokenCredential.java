package co.mgentertainment.common.apiclient.auth;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description RsaSignCredential
 */
public class RsaTokenCredential implements Credential {

    private final String tokenName;
    private final String publicKey;
    private final String clientId;
    private final Integer nonce;

    public RsaTokenCredential(String tokenName, String publicKey, String clientId, @Nullable Integer nonce) {
        if (StringUtils.isBlank(tokenName)) {
            throw new IllegalArgumentException("publicKey cannot be blank.");
        }
        this.tokenName = tokenName;
        if (StringUtils.isBlank(publicKey)) {
            throw new IllegalArgumentException("publicKey cannot be blank.");
        }
        this.publicKey = publicKey;
        if (StringUtils.isBlank(clientId)) {
            throw new IllegalArgumentException("clientId cannot be blank.");
        }
        this.clientId = clientId;
        this.nonce = nonce;
    }

    @Override
    public String getTokenName() {
        return this.tokenName;
    }

    @Override
    public String getEncryptKey() {
        return this.publicKey;
    }

    @Override
    public String getIdentity() {
        return this.clientId;
    }

    @Override
    public Integer getNonce() {
        return this.nonce;
    }
}
