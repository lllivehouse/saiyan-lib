package co.saiyan.common.schedulerplus.listener;

import co.saiyan.common.devent.DistributedEventCallback;
import co.saiyan.common.devent.annonation.DistributedEventListener;
import co.saiyan.common.schedulerplus.core.SchedulerPlusCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/27
 * @description RemoveSchedulerEventListener
 */
@DistributedEventListener(eventKey = SchedulerPlusEventKey.REMOVE)
@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveSchedulerEventListener implements DistributedEventCallback {

    private final SchedulerPlusCache schedulerPlusCache;

    @Override
    public void onComplete(String schedulerId) {
        ScheduledFuture scheduledFuture = schedulerPlusCache.getIdToScheduledFuture().get(schedulerId);
        if (scheduledFuture == null) {
            log.warn("任务{}不存在", schedulerId);
        }
        scheduledFuture.cancel(true);
        schedulerPlusCache.getIdToScheduledFuture().remove(schedulerId);
        log.info("任务{}已经终止", schedulerId);
    }
}
