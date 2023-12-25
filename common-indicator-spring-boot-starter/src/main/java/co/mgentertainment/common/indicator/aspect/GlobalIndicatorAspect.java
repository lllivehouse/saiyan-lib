package co.mgentertainment.common.indicator.aspect;

import co.mgentertainment.common.indicator.IndicatorCollector;
import co.mgentertainment.common.indicator.annotation.GlobalIndicator;
import co.mgentertainment.common.indicator.constant.IndicatorCounter;
import co.mgentertainment.common.indicator.utils.SpelExpressionResolver;
import co.mgentertainment.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author larry
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class GlobalIndicatorAspect implements ApplicationContextAware {

    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final RedisService redisService;

    private ApplicationContext applicationContext;

    @Around("@annotation(indicator)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint, GlobalIndicator indicator) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 方法形参名
        String[] argNames = Optional.ofNullable(nameDiscoverer.getParameterNames(method)).orElse(new String[0]);
        // 方法实参值
        Object[] argValues = joinPoint.getArgs();
        Object result;
        boolean error = false;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            error = true;
            throw e;
        } finally {
            if (!error) {
                try {
                    String itemId = SpelExpressionResolver.parseSpel(indicator.expressionToGetCategory(), argNames, argValues, this.applicationContext);
                    if (StringUtils.isNotBlank(itemId)) {
                        redisService.hIncr(IndicatorCollector.getGlobalIndicatorKey(indicator.name()), itemId, Long.valueOf(indicator.counter() == IndicatorCounter.INCREASE ? 1 : -1));
                    }
                } catch (Exception e) {
                    log.error("Global指标器[{}]采集异常", indicator.name(), e);
                }
            }
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
