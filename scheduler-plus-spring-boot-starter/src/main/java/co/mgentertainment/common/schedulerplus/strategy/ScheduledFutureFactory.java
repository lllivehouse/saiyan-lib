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
     * @param spTaskScheduler 定时任务线程池
     * @param executor        定时任务执行期
     */
    public static ScheduledFuture<?> create(ThreadPoolTaskScheduler spTaskScheduler, SchedulerPlusExecutor executor) {
        RunStrategyEnum strategy = executor.getMetadata().getStrategy();
        ScheduleStrategy scheduleStrategy;
        switch (strategy) {
            case ONE_TIME:
                scheduleStrategy = new OneTimeStrategy();
                break;
            case CRON_EXPRESSION:
                scheduleStrategy = new CronStrategy();
                break;
            default:
                throw new IllegalArgumentException("strategy not support");
        }
        return scheduleStrategy.schedule(spTaskScheduler, executor);
    }
}
