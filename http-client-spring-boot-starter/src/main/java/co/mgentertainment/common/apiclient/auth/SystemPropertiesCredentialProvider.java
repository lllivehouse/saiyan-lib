package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.core.ApiConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description SystemPropertiesCredentialProvider
 */
public class SystemPropertiesCredentialProvider implements CredentialProvider {

    @Override
    public Credential getCredential() {
        String publicKey = System.getProperty(ApiConstants.SYSTEM_PROPERTY_PUBLIC_KEY);
        String identity = System.getProperty(ApiConstants.SYSTEM_PROPERTY_IDENTITY);
        if (StringUtils.isAnyBlank(publicKey, identity)) {
            return null;
        }
        return new RsaTokenCredential(publicKey, identity);
    }
}
