package co.saiyan.common.mybatisplus.annonation;

import co.saiyan.common.mybatisplus.CommonMybatisPlusAutoConfiguration;
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
@Import(CommonMybatisPlusAutoConfiguration.class)
public @interface EnableCommonMybatisPlus {
}
