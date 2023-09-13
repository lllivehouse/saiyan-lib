package co.mgentertainment.common.devent.annonation;

import co.mgentertainment.common.devent.DistributedEventAutoConfiguration;
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
