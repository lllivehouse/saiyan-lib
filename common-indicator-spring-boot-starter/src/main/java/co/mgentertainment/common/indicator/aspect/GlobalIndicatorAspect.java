package co.mgentertainment.common.indicator.aspect;

import co.mgentertainment.common.indicator.annotation.GlobalIndicator;
import co.mgentertainment.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author larry
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class GlobalIndicatorAspect {

    private final RedisService redisService;

    @Around("@annotation(indicator)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint, GlobalIndicator indicator) {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            redisService.incr(indicator.name(), Long.valueOf(1));
        }
        return result;
    }
}
