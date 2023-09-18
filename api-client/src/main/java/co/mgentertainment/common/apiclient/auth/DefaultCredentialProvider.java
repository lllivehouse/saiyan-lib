package co.mgentertainment.common.apiclient.auth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description EnvironmentVariableCredentialProvider
 */
public class DefaultCredentialProvider implements CredentialProvider {

    private List<CredentialProvider> defaultProviders = new ArrayList();
    private static final List<CredentialProvider> USER_CONFIGURATION_PROVIDERS = new Vector();

    public DefaultCredentialProvider() {
        this.defaultProviders.add(new SystemPropertiesCredentialProvider());
        this.defaultProviders.add(new EnvironmentVariablesCredentialProvider());
    }

    @Override
    public Credential getCredential() {
        Credential credential;
        Iterator iterator;
        CredentialProvider provider;
        if (USER_CONFIGURATION_PROVIDERS.size() > 0) {
            iterator = USER_CONFIGURATION_PROVIDERS.iterator();
            while (iterator.hasNext()) {
                provider = (CredentialProvider) iterator.next();
                credential = provider.getCredential();
                if (null != credential) {
                    return credential;
                }
            }
        }
        iterator = this.defaultProviders.iterator();
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            provider = (CredentialProvider) iterator.next();
            credential = provider.getCredential();
        } while (null == credential);
        return credential;
    }

    public static boolean addCredentialProvider(CredentialProvider provider) {
        return USER_CONFIGURATION_PROVIDERS.add(provider);
    }

    public static boolean removeCredentialProvider(CredentialProvider provider) {
        return USER_CONFIGURATION_PROVIDERS.remove(provider);
    }

    public static boolean containsCredentialProvider(CredentialProvider provider) {
        return USER_CONFIGURATION_PROVIDERS.contains(provider);
    }

    public static void clearCredentialProvider() {
        USER_CONFIGURATION_PROVIDERS.clear();
    }
}