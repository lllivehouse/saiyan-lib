package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.exception.ClientException;
import co.mgentertainment.common.model.HttpClientConstants;
import co.mgentertainment.common.utils.SecurityHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * @author larry
 * @createTime 2022/12/9
 * @description RsaSigner
 */
public class RsaSigner extends Signer {

    @Override
    public String sign(Credential credential) {
        try {
            return SecurityHelper.rsaEncrypt(credential.getIdentity(), credential.getEncryptKey());
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

    @Override
    public String getTokenName() {
        return HttpClientConstants.SSE_HEADER_NAME;
    }

    @Override
    public String getTokenPrefix() {
        return StringUtils.EMPTY;
    }
}