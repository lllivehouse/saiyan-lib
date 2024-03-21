package co.saiyan.common.security.annonation;

import co.saiyan.common.security.config.GlobalSecurityConfiguration;
import co.saiyan.common.security.config.SecurityProviderConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableCommonSecurity
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SecurityProviderConfiguration.class, GlobalSecurityConfiguration.class})
public @interface EnableCommonSecurity {
}
