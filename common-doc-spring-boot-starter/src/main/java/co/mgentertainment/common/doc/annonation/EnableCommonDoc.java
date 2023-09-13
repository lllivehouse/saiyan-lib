package co.mgentertainment.common.doc.annonation;

import co.mgentertainment.common.doc.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableCommonDoc
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SwaggerAutoConfiguration.class)
public @interface EnableCommonDoc {
}
