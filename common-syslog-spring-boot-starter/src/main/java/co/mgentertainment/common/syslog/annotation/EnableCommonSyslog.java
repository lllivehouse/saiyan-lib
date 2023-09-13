package co.mgentertainment.common.syslog.annotation;

import co.mgentertainment.common.syslog.SyslogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description EnableCommonSecurity
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SyslogAutoConfiguration.class)
public @interface EnableCommonSyslog {
}
