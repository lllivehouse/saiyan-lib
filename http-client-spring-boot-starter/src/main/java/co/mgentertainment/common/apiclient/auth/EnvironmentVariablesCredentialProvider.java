package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.core.ApiConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description EnvironmentVariableCredentialProvider
 */
public class EnvironmentVariablesCredentialProvider implements CredentialProvider {

    @Override
    public Credential getCredential() {
        String publicKey = System.getenv(ApiConstants.ENV_PUBLIC_KEY);
        String identity = System.getenv(ApiConstants.ENV_IDENTITY);
        if (StringUtils.isAnyBlank(publicKey, identity)) {
            return null;
        }
        return new RsaTokenCredential(publicKey, identity);
    }
}