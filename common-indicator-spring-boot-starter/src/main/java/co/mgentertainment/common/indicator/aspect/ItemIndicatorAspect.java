package co.mgentertainment.common.indicator.aspect;

import co.mgentertainment.common.indicator.annotation.ItemIndicator;
import co.mgentertainment.common.indicator.utils.SpelExpressionResolver;
import co.mgentertainment.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author larry
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ItemIndicatorAspect {

    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final RedisService redisService;

    @Around("@annotation(indicator)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint, ItemIndicator indicator) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 方法形参名
        String[] argNames = Optional.ofNullable(nameDiscoverer.getParameterNames(method)).orElse(new String[0]);
        // 方法实参值
        Object[] argValues = joinPoint.getArgs();

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                String itemId = SpelExpressionResolver.parseSpel(indicator.expressionToGetItem(), argNames, argValues);
                redisService.hIncr(indicator.name().getKey(), itemId, Long.valueOf(1));
            } catch (Exception e) {
                log.error("指标器[{}]采集异常", indicator.name(), e);
            }
        }
        return result;
    }
}
