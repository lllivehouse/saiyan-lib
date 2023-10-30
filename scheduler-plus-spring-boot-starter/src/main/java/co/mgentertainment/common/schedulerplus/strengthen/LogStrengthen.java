package co.mgentertainment.common.schedulerplus.strengthen;

import co.mgentertainment.common.model.R;
import co.mgentertainment.common.schedulerplus.annontation.StrengthenOrder;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusMeta;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusTaskStatusEnum;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusLogRepository;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description LogStrengthen
 */
@StrengthenOrder(2)
@RequiredArgsConstructor
public class LogStrengthen implements SchedulerPlusStrength {

    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;
    private final SchedulerPlusLogRepository schedulerPlusLogRepository;

    @Override
    public void before(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata) {
        schedulerPlusTaskRepository.updateTaskStatus(metadata.getSchedulerId(), SchedulerPlusTaskStatusEnum.IN_PROGRESS);
        schedulerPlusLogRepository.createLog(metadata.getSchedulerId());
    }

    @Override
    public void exception(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata) {
        schedulerPlusLogRepository.updateLog(metadata.getSchedulerId(), 1, "exception", new Date());
    }

    @Override
    public void afterFinally(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata, Object result) {
        schedulerPlusTaskRepository.updateTaskStatus(metadata.getSchedulerId(), SchedulerPlusTaskStatusEnum.DONE);
        if (result instanceof R) {
            schedulerPlusLogRepository.updateLog(metadata.getSchedulerId(), ((R) result).getCode(), ((R) result).getMsg(), new Date());
        }
    }

    @Override
    public void after(Object bean, Method method, Object[] args, SchedulerPlusMeta metadata) {
        schedulerPlusLogRepository.updateLog(metadata.getSchedulerId(), 0, StringUtils.EMPTY, new Date());
    }
}
