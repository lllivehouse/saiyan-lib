package co.mgentertainment.common.schedulerplus.proxy;

import co.mgentertainment.common.model.R;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import co.mgentertainment.common.schedulerplus.strengthen.SchedulerPlusStrength;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description SchedulerPlusExecutorProxy
 */
@Slf4j
@RequiredArgsConstructor
public class SchedulerPlusExecutorProxy implements MethodInterceptor {

    private final List<SchedulerPlusStrength> strengthens;

    public SchedulerPlusExecutor createProxy(SchedulerPlusMeta metadata) {
        Enhancer enhancer = new Enhancer();
        // 设置代理目标
        enhancer.setSuperclass(SchedulerPlusExecutor.class);
        // 设置单一回调对象，在调用中拦截对目标方法的调用
        enhancer.setCallback(this);
        // 设置类加载器
        enhancer.setClassLoader(SchedulerPlusExecutor.class.getClassLoader());
        // 创建有参构造函数的代理对象
        return (SchedulerPlusExecutor) enhancer.create(new Class[]{SchedulerPlusMeta.class}, new Object[]{metadata});
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!"invoke".equals(method.getName())) {
            return methodProxy.invokeSuper(obj, args);
        }
        Object result = null;
        AtomicLong logId = new AtomicLong();
        boolean lockObtained = true;
        for (SchedulerPlusStrength strengthen : strengthens) {
            try {
                R<Long> res = strengthen.before(obj, method, args);
                if (!res.isSuccess()) {
                    lockObtained = false;
                    break;
                }
                if (res.getData() != null) {
                    logId.set(res.getData());
                }
            } catch (Exception e) {
                log.debug("before strengthen [{}] error", strengthen.getClass(), e);
                break;
            }
        }
        try {
            if (lockObtained) {
                result = methodProxy.invokeSuper(obj, args);
            }
        } catch (Exception e) {
            log.error("proxy obj [{}] execution error", obj.getClass().getName(), e);
            for (SchedulerPlusStrength strengthen : strengthens) {
                try {
                    strengthen.exception(obj, method, args, logId.get());
                } catch (Exception ex) {
                    log.error("exception strengthen [{}] error", strengthen.getClass(), ex);
                    break;
                }
            }
        } finally {
            Object finalResult = result;
            for (SchedulerPlusStrength strengthen : strengthens) {
                try {
                    strengthen.afterFinally(obj, method, args, finalResult, logId.get());
                } catch (Exception e) {
                    log.error("afterFinally strengthen [{}] error", strengthen.getClass(), e);
                    break;
                }
            }
        }
        for (SchedulerPlusStrength strengthen : strengthens) {
            try {
                strengthen.after(obj, method, args, logId.get());
            } catch (Exception e) {
                log.error("after strengthen [{}] error", strengthen.getClass(), e);
                break;
            }
        }
        return result;
    }
}
