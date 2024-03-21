package co.saiyan.common.schedulerplus;

import co.saiyan.common.devent.DistributedEventProvider;
import co.saiyan.common.model.R;
import co.saiyan.common.schedulerplus.core.SchedulerPlusCache;
import co.saiyan.common.schedulerplus.core.SchedulerPlusExecutor;
import co.saiyan.common.schedulerplus.exception.SchedulerPlusException;
import co.saiyan.common.schedulerplus.listener.SchedulerPlusEventKey;
import co.saiyan.common.schedulerplus.strategy.RunStrategyEnum;
import co.saiyan.common.schedulerplus.support.*;
import co.saiyan.common.utils.GsonFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SchedulerPlusManager {

    private final SchedulerPlusCache schedulerPlusCache;

    private final DistributedEventProvider distributedEventProvider;

    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;

    private final SchedulerPlusLogRepository schedulerPlusLogRepository;

    /**
     * 查询所有启动的SchedulerId
     */
    public List<String> getRunningSchedulerIds() {
        return new ArrayList<>(schedulerPlusCache.getIdToScheduledFuture().keySet());
    }

    /**
     * 查询所有的SchedulerId
     */
    public List<String> getAllSchedulerIds() {
        return new ArrayList<>(schedulerPlusCache.getIdToSchedulerPlusExecutor().keySet());
    }

    /**
     * 添加定时任务
     *
     * @param item
     */
    public void addScheduler(SchedulerPlusTaskItem item) {
        Preconditions.checkArgument(item != null, "定时任务不能为空");
        boolean cronInvalidOrExpired = StrategyValueUtils.isStrategyValueInvalidOrExpired(item.getStrategy(), item.getStrategyValue());
        if (cronInvalidOrExpired) {
            throw new SchedulerPlusException("策略值无效或者已经过期");
        }
        String schedulerId = item.getSchedulerId();
        if (getAllSchedulerIds().contains(schedulerId)) {
            throw new SchedulerPlusException("定时任务" + schedulerId + "已经被启动过了");
        }

        try {
            schedulerPlusTaskRepository.createTask(SchedulerPlusObjectMapper.INSTANCE.toSchedulerPlusTaskDO(item));
            distributedEventProvider.fire(SchedulerPlusEventKey.ADD, GsonFactory.getGson().toJson(item));
        } catch (Exception e) {
            log.error("添加定时任务事件失败", e);
        }
    }

    /**
     * 手动执行一次任务,单机执行
     *
     * @param schedulerId
     */
    public R<Boolean> runScheduler(String schedulerId) {
        SchedulerPlusExecutor executor = schedulerPlusCache.getIdToSchedulerPlusExecutor().get(schedulerId);
        if (executor == null) {
            throw new SchedulerPlusException("任务" + schedulerId + "不存在");
        }
        return executor.invoke();
    }

    /**
     * 停止并移除scheduledFuture
     *
     * @param schedulerId
     */
    public void removeScheduler(String schedulerId) {
        if (!getAllSchedulerIds().contains(schedulerId)) {
            throw new SchedulerPlusException("定时任务" + schedulerId + "不存在");
        }
        try {
            distributedEventProvider.fire(SchedulerPlusEventKey.REMOVE, schedulerId);
        } catch (NacosException e) {
            log.error("删除定时任务事件失败", e);
        }
        schedulerPlusTaskRepository.removeTask(schedulerId);
    }

    /**
     * 修改Scheduled的执行周期
     *
     * @param schedulerId
     * @param strategy
     * @param strategyValue
     */
    public void updateSchedulerTime(String schedulerId, RunStrategyEnum strategy, String strategyValue) {
        schedulerPlusTaskRepository.updateTaskStrategy(schedulerId, strategy, strategyValue);
        SchedulerPlusTaskItem item = SchedulerPlusTaskItem.builder().schedulerId(schedulerId).strategy(strategy).strategyValue(strategyValue).build();
        try {
            distributedEventProvider.fire(SchedulerPlusEventKey.UPDATE_CRON, GsonFactory.getGson().toJson(item));
        } catch (NacosException e) {
            log.error("修改定时任务事件失败", e);
        }
    }

    /**
     * 获取某个scheduler日志信息
     *
     * @param schedulerId
     * @param lastRecordCreateTime
     * @param limit
     */
    public List<SchedulerPlusLogDO> getScheduledLogs(String schedulerId, Date lastRecordCreateTime, int limit) {
        return schedulerPlusLogRepository.listLog(schedulerId, lastRecordCreateTime, limit);
    }
}
