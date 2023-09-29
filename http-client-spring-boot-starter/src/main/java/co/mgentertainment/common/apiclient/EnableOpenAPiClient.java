package co.mgentertainment.common.apiclient;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableOpenAPiClient
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(OpenApiClientConfiguration.class)
public @interface EnableOpenAPiClient {
}
