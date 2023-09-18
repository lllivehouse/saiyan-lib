package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.core.ApiConstants;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description EnvironmentVariableCredentialProvider
 */
public class EnvironmentVariablesCredentialProvider implements CredentialProvider {

    @Override
    public Credential getCredential() {
        return new RsaTokenCredential(System.getenv(ApiConstants.ENV_PUBLIC_KEY), System.getenv(ApiConstants.ENV_IDENTITY));
    }
}