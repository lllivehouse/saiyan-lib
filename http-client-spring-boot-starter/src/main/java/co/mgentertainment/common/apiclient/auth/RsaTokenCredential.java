package co.mgentertainment.common.apiclient.auth;

import java.util.Objects;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description RsaSignCredential
 */
public class RsaTokenCredential implements Credential {

    private final String publicKey;
    private final String clientId;

    public RsaTokenCredential(String publicKey, String clientId) {
        if (Objects.isNull(publicKey)) {
            throw new IllegalArgumentException("publicKey cannot be null.");
        }
        if (Objects.isNull(clientId)) {
            throw new IllegalArgumentException("clientId cannot be null.");
        }
        this.publicKey = publicKey;
        this.clientId = clientId;
    }

    @Override
    public String getEncryptKey() {
        return this.publicKey;
    }

    @Override
    public String getIdentity() {
        return this.clientId;
    }
}
