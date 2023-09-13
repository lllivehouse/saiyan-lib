package co.mgentertainment.common.uidgen.annonation;

import co.mgentertainment.common.uidgen.config.UidGenAutoConfiguration;
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
@Import(UidGenAutoConfiguration.class)
public @interface EnableUidGenerator {
}
