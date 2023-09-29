package co.mgentertainment.common.indicator.annotation;

import co.mgentertainment.common.indicator.config.IndicatorAutoConfiguration;
import co.mgentertainment.common.redis.config.CommonRedisAutoConfiguration;
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
@Import({IndicatorAutoConfiguration.class, CommonRedisAutoConfiguration.class})
public @interface EnableCommonIndicator {


}
