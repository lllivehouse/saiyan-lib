package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.exception.ClientException;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description CredentialProvider
 */
public interface CredentialProvider {
    /**
     * getCredential
     * @return
     * @throws ClientException
     */
    Credential getCredential();
}
