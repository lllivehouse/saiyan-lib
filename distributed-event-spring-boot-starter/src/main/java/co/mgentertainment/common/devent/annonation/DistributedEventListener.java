package co.mgentertainment.common.devent.annonation;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 2023/8/24
 * @description DistributedEventListener
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedEventListener {
    String eventKey() default "";
}
