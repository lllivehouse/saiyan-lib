package co.mgentertainment.common.schedulerplus.annontation;

import co.mgentertainment.common.schedulerplus.SchedulerPlusAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 10/27/2023
 * @description EnableSchedulerPlus
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SchedulerPlusAutoConfiguration.class)
public @interface EnableSchedulerPlus {
}
