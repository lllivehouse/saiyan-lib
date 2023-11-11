package co.mgentertainment.dlock.annotation;

import co.mgentertainment.dlock.DistributedLockAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableCommonIndicator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DistributedLockAutoConfiguration.class})
public @interface EnableDistributedLock {
}
