package co.mgentertainment.common.schedulerplus.strategy;


import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description CronStrategy
 */
public class CronStrategy implements ScheduleStrategy {

    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, SchedulerPlusExecutor executor) {
        String cronExpression = Optional.ofNullable(executor.getMetadata()).orElse(SchedulerPlusMeta.builder().build()).getCronExpression();
        return threadPoolTaskScheduler.schedule(() -> executor.invoke(), new CronTrigger(cronExpression));
    }
}
