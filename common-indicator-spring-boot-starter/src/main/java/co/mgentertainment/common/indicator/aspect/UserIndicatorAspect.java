package co.mgentertainment.common.indicator.aspect;

import co.mgentertainment.common.indicator.annotation.UserIndicator;
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
public class UserIndicatorAspect {

    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final RedisService redisService;

    @Around("@annotation(indicator)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint, UserIndicator indicator) {
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
            String username = SpelExpressionResolver.parseSpel(indicator.expressionToGetUser(), argNames, argValues);
            redisService.hIncr(indicator.name(), username, Long.valueOf(1));
        }
        return result;
    }
}
