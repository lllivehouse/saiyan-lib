package co.mgentertainment.common.schedulerplus.listener;

import co.mgentertainment.common.devent.DistributedEventCallback;
import co.mgentertainment.common.devent.annonation.DistributedEventListener;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusCache;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusObjectMapper;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskDO;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskItem;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskRepository;
import co.mgentertainment.common.utils.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description UpdateSchedulerCronEventListener
 */
@DistributedEventListener(eventKey = SchedulerPlusEventKey.UPDATE_CRON)
@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateSchedulerCronEventListener implements DistributedEventCallback {

    private final SchedulerPlusCache schedulerPlusCache;
    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void onComplete(String json) {
        SchedulerPlusTaskItem item = GsonFactory.getGson().fromJson(json, SchedulerPlusTaskItem.class);
        if (item == null || StringUtils.isBlank(item.getSchedulerId())) {
            log.warn("参数异常：{}", json);
            return;
        }
        String schedulerId = item.getSchedulerId();
        SchedulerPlusExecutor executor = schedulerPlusCache.getIdToSchedulerPlusExecutor().get(schedulerId);
        if (executor == null) {
            log.warn("任务{}不存在", schedulerId);
        }
        // 终止原先的任务
        ScheduledFuture scheduledFuture = schedulerPlusCache.getIdToScheduledFuture().get(schedulerId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            schedulerPlusCache.getIdToScheduledFuture().remove(schedulerId);
        }
        // 创建新的任务
        SchedulerPlusMeta metadata = executor.getMetadata();
        SchedulerPlusTaskDO task = SchedulerPlusObjectMapper.INSTANCE.toSchedulerPlusTaskDO(metadata);
        schedulerPlusTaskRepository.startTask(task, schedulerPlusCache, threadPoolTaskScheduler);
    }
}
