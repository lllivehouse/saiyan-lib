package co.mgentertainment.common.schedulerplus.strengthen;

import co.mgentertainment.common.schedulerplus.annontation.StrengthenOrder;
import co.mgentertainment.common.schedulerplus.core.ScheduledModeEnum;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.exception.SchedulerPlusException;
import co.mgentertainment.dlock.registry.LockRegistry;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description LockStrengthen
 */
@StrengthenOrder(1)
@RequiredArgsConstructor
public class LockStrengthen implements SchedulerPlusStrength {

    private final LockRegistry lockRegistry;

    private Lock lock;
    private final transient AtomicBoolean locked = new AtomicBoolean(false);

    @Override
    public Long before(Object bean, Method method, Object[] args) {
        Preconditions.checkArgument(bean instanceof SchedulerPlusExecutor, "invalid proxy bean");
        SchedulerPlusExecutor executor = (SchedulerPlusExecutor) bean;
        Preconditions.checkArgument(executor.getMetadata() != null && StringUtils.isNotBlank(executor.getMetadata().getSchedulerId()), "schedulerId can not be blank");
        if (ScheduledModeEnum.SINGLE_INSTANCE.equals(executor.getMetadata().getScheduledMode())) {
            this.lock = lockRegistry.obtain(executor.getMetadata().getSchedulerId());
            // 获取锁
            if (!this.lock.tryLock()) {
                throw new SchedulerPlusException("fail to obtain distributed-lock");
            }
            locked.set(true);
        }
        return null;
    }

    @Override
    public void exception(Object bean, Method method, Object[] args, Long id) {
    }

    @Override
    public void afterFinally(Object bean, Method method, Object[] args, Object result, Long id) {
        if (this.lock != null && locked.get()) {
            // 释放锁
            lock.unlock();
        }
    }

    @Override
    public void after(Object bean, Method method, Object[] args, Long id) {
    }
}
