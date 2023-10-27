package co.mgentertainment.common.schedulerplus.listener;

import co.mgentertainment.common.devent.DistributedEventCallback;
import co.mgentertainment.common.devent.annonation.DistributedEventListener;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusCache;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.exception.SchedulerPlusException;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusObjectMapper;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskDO;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskItem;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskRepository;
import co.mgentertainment.common.utils.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description AddSchedulerEventListener
 */
@DistributedEventListener(eventKey = SchedulerPlusEventKey.ADD)
@Component
@RequiredArgsConstructor
public class AddSchedulerEventListener implements DistributedEventCallback {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;

    private final SchedulerPlusCache schedulerPlusCache;

    @Override
    public void onComplete(String value) {
        SchedulerPlusTaskItem item = GsonFactory.getGson().fromJson(value, SchedulerPlusTaskItem.class);
        String schedulerId = item.getSchedulerId();
        SchedulerPlusExecutor executor = schedulerPlusCache.getIdToSchedulerPlusExecutor().get(schedulerId);
        if (executor != null) {
            throw new SchedulerPlusException("定时任务" + schedulerId + "已经被启动过了");
        }
        SchedulerPlusTaskDO task = SchedulerPlusObjectMapper.INSTANCE.toSchedulerPlusTaskDO(item);
        schedulerPlusTaskRepository.startTask(task, schedulerPlusCache, threadPoolTaskScheduler);
    }
}
