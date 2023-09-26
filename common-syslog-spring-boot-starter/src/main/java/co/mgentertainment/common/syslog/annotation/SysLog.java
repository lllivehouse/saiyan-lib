package co.mgentertainment.common.syslog.annotation;

import java.lang.annotation.*;

/**
 * @author larry
 * @desc 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 描述
     *
     * @return {String}
     */
    String value() default "";

    /**
     * spel 表达式
     *
     * @return 日志描述
     */
    String expression() default "";

    /**
     * 是否忽略请求参数（针对file上传）
     *
     * @return
     */
    boolean ignoredArgs() default false;
}
