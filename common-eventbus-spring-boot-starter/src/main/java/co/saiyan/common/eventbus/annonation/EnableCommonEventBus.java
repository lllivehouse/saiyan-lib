package co.saiyan.common.eventbus.annonation;

import co.saiyan.common.eventbus.CommonEventBusConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableCommonEventBus
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CommonEventBusConfiguration.class)
public @interface EnableCommonEventBus {
}
