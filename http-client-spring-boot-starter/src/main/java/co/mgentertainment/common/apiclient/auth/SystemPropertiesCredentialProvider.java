package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.core.ApiConstants;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description SystemPropertiesCredentialProvider
 */
public class SystemPropertiesCredentialProvider implements CredentialProvider {

    @Override
    public Credential getCredential() {
        return new RsaTokenCredential(System.getProperty(ApiConstants.SYSTEM_PROPERTY_PUBLIC_KEY), System.getProperty(ApiConstants.SYSTEM_PROPERTY_IDENTITY));
    }
}
