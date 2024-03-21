package co.saiyan.common.schedulerplus.strategy;

import co.saiyan.common.schedulerplus.core.SchedulerPlusExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description ScheduleStrategy
 */
public interface ScheduleStrategy {
    ScheduledFuture<?> schedule(ThreadPoolTaskScheduler spTaskScheduler, SchedulerPlusExecutor executor);
}
