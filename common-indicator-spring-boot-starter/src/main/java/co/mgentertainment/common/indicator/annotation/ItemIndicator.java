package co.mgentertainment.common.indicator.annotation;

import co.mgentertainment.common.indicator.constant.IndicatorCategory;
import co.mgentertainment.common.indicator.constant.IndicatorName;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @author larry
 * @desc 关联指标采集注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ItemIndicator {

    /**
     * 指标分类
     *
     * @return {String}
     */
    IndicatorCategory type() default IndicatorCategory.USER;

    /**
     * 指标名称
     *
     * @return {String}
     */
    IndicatorName name() default IndicatorName.LIKES;

    /**
     * spel表达式获取当前关联项 如uid vid pid sid等
     *
     * @return
     */
    String expressionToGetItem() default StringUtils.EMPTY;
}