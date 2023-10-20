package co.mgentertainment.common.indicator.annotation;

import co.mgentertainment.common.indicator.constant.GlobalIndicatorName;
import co.mgentertainment.common.indicator.constant.IndicatorCounter;

import java.lang.annotation.*;

/**
 * @author larry
 * @desc 全局指标采集注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalIndicator {

    /**
     * 指标名称
     *
     * @return {String}
     */
    GlobalIndicatorName name() default GlobalIndicatorName.PAGE_VIEWS;

    /**
     * 计数器类型
     *
     * @return
     */
    IndicatorCounter counter() default IndicatorCounter.INCREASE;

}