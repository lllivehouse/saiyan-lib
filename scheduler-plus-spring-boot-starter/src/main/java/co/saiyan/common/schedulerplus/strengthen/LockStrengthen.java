package co.saiyan.common.schedulerplus.strengthen;

import co.saiyan.common.model.R;
import co.saiyan.common.schedulerplus.annontation.StrengthenOrder;
import co.saiyan.common.schedulerplus.core.ScheduledModeEnum;
import co.saiyan.common.schedulerplus.core.SchedulerPlusExecutor;
import co.saiyan.dlock.registry.LockRegistry;
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
    public R<Long> before(Object bean, Method method, Object[] args) {
        if (!(bean instanceof SchedulerPlusExecutor)) {
            return R.failed("invalid proxy bean");
        }
        SchedulerPlusExecutor executor = (SchedulerPlusExecutor) bean;
        if (executor.getMetadata() == null || StringUtils.isBlank(executor.getMetadata().getSchedulerId())) {
            return R.failed("schedulerId can not be blank");
        }
        if (ScheduledModeEnum.SINGLE_INSTANCE.equals(executor.getMetadata().getScheduledMode())) {
            this.lock = lockRegistry.obtain(executor.getMetadata().getSchedulerId());
            // 获取锁
            if (!this.lock.tryLock()) {
                return R.failed("fail to obtain distributed-lock");
            }
            locked.set(true);
        }
        return R.ok();
    }

    @Override
    public void exception(Object bean, Method method, Object[] args, Long id) {
    }

    @Override
    public void afterFinally(Object bean, Method method, Object[] args, Object result, Long id) {
        if (this.lock != null && locked.get()) {
            // 释放锁
            this.lock.unlock();
        }
    }

    @Override
    public void after(Object bean, Method method, Object[] args, Long id) {
    }
}
