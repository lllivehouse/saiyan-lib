package co.saiyan.common.devent.annonation;

import co.saiyan.common.devent.DistributedEventAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableDistributedEvent
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DistributedEventAutoConfiguration.class)
public @interface EnableDistributedEvent {
}
