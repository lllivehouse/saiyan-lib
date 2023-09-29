package co.mgentertainment.common.indicator.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @author larry
 * @desc 用户指标采集注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserIndicator {

    /**
     * 指标名称
     *
     * @return {String}
     */
    String name() default StringUtils.EMPTY;

    /**
     * spel表达式获取当前用户
     *
     * @return
     */
    String expressionToGetUser() default StringUtils.EMPTY;
}