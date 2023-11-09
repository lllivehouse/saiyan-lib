package co.mgentertainment.common.schedulerplus.strategy;


import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description CronStrategy
 */
public class OneTimeStrategy implements ScheduleStrategy {

    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, SchedulerPlusExecutor executor) {
        String invokeTimestamp = Optional.ofNullable(executor.getMetadata()).orElse(SchedulerPlusMeta.builder().build()).getStrategyValue();
        return threadPoolTaskScheduler.schedule(() -> executor.invoke(), new Date(Long.valueOf(invokeTimestamp)));
    }
}
