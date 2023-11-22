package co.mgentertainment.common.schedulerplus.strategy;


import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import co.mgentertainment.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description CronStrategy
 */
@Slf4j
public class OneTimeStrategy implements ScheduleStrategy {

    @Override
    public ScheduledFuture<?> schedule(@Qualifier("spTaskScheduler") ThreadPoolTaskScheduler spTaskScheduler, SchedulerPlusExecutor executor) {
        String invokeTimestamp = Optional.ofNullable(executor.getMetadata()).orElse(SchedulerPlusMeta.builder().build()).getStrategyValue();
        Date startTime = new Date(Long.valueOf(invokeTimestamp));
        log.info("启动OneTimeStrategy定时任务, 触发时间: {}", DateUtils.format(startTime));
        return spTaskScheduler.schedule(() -> executor.invoke(), startTime);
    }
}
