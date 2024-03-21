package co.saiyan.common.indicator.aspect;

import co.saiyan.common.indicator.IndicatorCollector;
import co.saiyan.common.indicator.annotation.ItemIndicator;
import co.saiyan.common.indicator.constant.IndicatorCounter;
import co.saiyan.common.indicator.utils.SpelExpressionResolver;
import co.saiyan.common.redis.service.RedisService;
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
public class ItemIndicatorAspect implements ApplicationContextAware {

    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final RedisService redisService;

    private ApplicationContext applicationContext;

    @Around("@annotation(indicator)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint, ItemIndicator indicator) {
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
                    String itemId = SpelExpressionResolver.parseSpel(indicator.expressionToGetItem(), argNames, argValues, this.applicationContext);
                    if (StringUtils.isNotBlank(itemId)) {
                        redisService.hIncr(IndicatorCollector.getItemIndicatorKey(indicator.type(), indicator.name()), itemId, Long.valueOf(indicator.counter() == IndicatorCounter.INCREASE ? 1 : -1));
                    }
                } catch (Exception e) {
                    log.error("Item指标器[{}]采集异常", indicator.type().getCategory() + indicator.name().getValue(), e);
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
