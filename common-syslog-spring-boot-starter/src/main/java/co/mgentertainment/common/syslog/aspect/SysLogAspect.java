package co.mgentertainment.common.syslog.aspect;

import co.mgentertainment.common.syslog.annotation.SysLog;
import co.mgentertainment.common.syslog.dal.po.SysLogDO;
import co.mgentertainment.common.syslog.event.SysLogEvent;
import co.mgentertainment.common.syslog.util.LogTypeEnum;
import co.mgentertainment.common.syslog.util.SpringContextHolder;
import co.mgentertainment.common.syslog.util.SysLogUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

/**
 * @author larry
 */
@Aspect
@Slf4j
public class SysLogAspect {

    @Around("@annotation(sysLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

        String value = sysLog.value();
        String expression = sysLog.expression();
        // 当前表达式存在 SPEL，会覆盖 value 的值
        if (StringUtils.isNotBlank(expression)) {
            // 解析SPEL
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
            try {
                value = SysLogUtils.getValue(context, expression, String.class);
            } catch (Exception e) {
                // SPEL 表达式异常，获取 value 的值
                log.error("@SysLog 解析SPEL {} 异常", expression);
            }
        }
        String[] split = value.split("_");
        SysLogDO logVo = SysLogUtils.getSysLog(point.getArgs(), sysLog.ignoredArgs());
        if (split.length == 3) {
            String oprationModule = split[0];
            String oprationText = split[1];
            String oprationtType = split[2];

            logVo.setOprationModule(oprationModule);
            logVo.setOprationText(oprationText);
            logVo.setOprationType(oprationtType);
        }
        logVo.setTitle(value);


        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj;

        try {
            obj = point.proceed();
        } catch (Exception e) {
            logVo.setType(LogTypeEnum.ERROR.getType());
            logVo.setException(e.getMessage());
            throw e;
        } finally {
            Long endTime = System.currentTimeMillis();
            logVo.setTime(endTime - startTime);
            logVo.setServiceId(System.getProperty("app.name", ""));
            SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        }

        return obj;
    }

}
