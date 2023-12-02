package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.utils.SecurityHelper;
import org.apache.commons.lang3.RandomUtils;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description RsaSigner
 */
public class RsaSigner extends Signer {

    @Override
    public String sign(Credential credential) {
        try {
            String identity = credential.getNonce() != null ? identityAddNonce(credential.getIdentity(), credential.getNonce()) : credential.getIdentity();
            return SecurityHelper.rsaEncrypt(identity, credential.getEncryptKey());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String sign(String contentToSign, Credential credential) throws ClientException {
        throw new ClientException("operation not support");
    }

    @Override
    public String resign(String oldToken, Credential credential) throws ClientException {
        throw new ClientException("operation not support");
    }

    private String identityAddNonce(String identity, Integer nonce) {
        return identity + ';' + (System.currentTimeMillis() + RandomUtils.nextInt(3000, nonce * 1000));
    }
}