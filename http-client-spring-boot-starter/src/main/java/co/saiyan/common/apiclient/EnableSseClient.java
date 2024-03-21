package co.saiyan.common.apiclient;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableSseClient
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ConfigCenterSseClientConfiguration.class, PaySseClientConfiguration.class})
public @interface EnableSseClient {
}
