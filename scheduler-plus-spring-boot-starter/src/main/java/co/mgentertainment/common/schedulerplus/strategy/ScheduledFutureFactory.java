package co.mgentertainment.common.schedulerplus.strategy;

import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description ScheduledFutureFactory
 */
public class ScheduledFutureFactory {

    /**
     * 策略启动定时任务
     *
     * @param threadPoolTaskScheduler 定时任务线程池
     * @param executor                定时任务执行期
     */
    public static ScheduledFuture<?> create(ThreadPoolTaskScheduler threadPoolTaskScheduler, SchedulerPlusExecutor executor) {
        ScheduleStrategy scheduleStrategy = new CronStrategy();
        return scheduleStrategy.schedule(threadPoolTaskScheduler, executor);
    }
}
