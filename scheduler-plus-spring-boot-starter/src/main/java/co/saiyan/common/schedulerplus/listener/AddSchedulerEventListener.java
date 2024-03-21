package co.saiyan.common.schedulerplus.listener;

import co.saiyan.common.devent.DistributedEventCallback;
import co.saiyan.common.devent.annonation.DistributedEventListener;
import co.saiyan.common.schedulerplus.core.SchedulerPlusCache;
import co.saiyan.common.schedulerplus.core.SchedulerPlusExecutor;
import co.saiyan.common.schedulerplus.exception.SchedulerPlusException;
import co.saiyan.common.schedulerplus.support.SchedulerPlusObjectMapper;
import co.saiyan.common.schedulerplus.support.SchedulerPlusTaskDO;
import co.saiyan.common.schedulerplus.support.SchedulerPlusTaskItem;
import co.saiyan.common.schedulerplus.support.SchedulerPlusTaskRepository;
import co.saiyan.common.utils.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AddSchedulerEventListener implements DistributedEventCallback {

    private final ThreadPoolTaskScheduler spTaskScheduler;

    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;

    private final SchedulerPlusCache schedulerPlusCache;

    @Override
    public void onComplete(String value) {
        log.info("监听到添加定时任务: {}", value);
        SchedulerPlusTaskItem item = GsonFactory.getGson().fromJson(value, SchedulerPlusTaskItem.class);
        String schedulerId = item.getSchedulerId();
        SchedulerPlusExecutor executor = schedulerPlusCache.getIdToSchedulerPlusExecutor().get(schedulerId);
        if (executor != null) {
            log.error("定时任务: {}已经被启动过了", schedulerId);
            throw new SchedulerPlusException("定时任务" + schedulerId + "已经被启动过了");
        }
        SchedulerPlusTaskDO task = SchedulerPlusObjectMapper.INSTANCE.toSchedulerPlusTaskDO(item);
        schedulerPlusTaskRepository.startTask(task, schedulerPlusCache, spTaskScheduler);
    }
}
