package co.saiyan.common.schedulerplus.annontation;

import java.lang.annotation.*;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description StrengthenOrder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface StrengthenOrder {
    int value() default 0;
}