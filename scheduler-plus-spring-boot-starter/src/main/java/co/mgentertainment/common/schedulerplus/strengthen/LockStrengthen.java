package co.mgentertainment.common.schedulerplus.strengthen;

import co.mgentertainment.common.schedulerplus.annontation.StrengthenOrder;
import co.mgentertainment.common.schedulerplus.core.ScheduledModeEnum;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import co.mgentertainment.common.schedulerplus.exception.SchedulerPlusException;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.support.locks.LockRegistry;

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
    public void before(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata) {
        Preconditions.checkArgument(StringUtils.isNotBlank(metadata.getSchedulerId()), "schedulerId can not be blank");
        if (ScheduledModeEnum.SINGLE_INSTANCE.equals(metadata.getScheduledMode())) {
            this.lock = lockRegistry.obtain(metadata.getSchedulerId());
            // 获取锁
            if (!this.lock.tryLock()) {
                throw new SchedulerPlusException("fail to obtain distributed-lock");
            }
            locked.set(true);
        }
    }

    @Override
    public void exception(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata) {

    }

    @Override
    public void afterFinally(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata, Object result) {
        if (this.lock != null && locked.get()) {
            // 释放锁
            lock.unlock();
        }
    }

    @Override
    public void after(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata) {

    }
}
