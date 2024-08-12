package co.saiyan.common.kafka.annonation;

import co.saiyan.common.kafka.config.CommonKafkaAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lex
 * @createTime 2024/05/23
 * @description EnableCommonKafka
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CommonKafkaAutoConfiguration.class)
public @interface EnableCommonKafka {
}
