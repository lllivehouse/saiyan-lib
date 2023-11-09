package co.mgentertainment.common.schedulerplus.strengthen;

import co.mgentertainment.common.model.R;
import co.mgentertainment.common.schedulerplus.annontation.StrengthenOrder;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusExecutor;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusTaskStatusEnum;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusLogRepository;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskRepository;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

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
    public void before(Object bean, Method method, Object[] args) {
        String schedulerId = getSchedulerId(bean);
        schedulerPlusTaskRepository.updateTaskStatus(schedulerId, SchedulerPlusTaskStatusEnum.IN_PROGRESS);
        schedulerPlusLogRepository.createLog(schedulerId);
    }

    @Override
    public void exception(Object bean, Method method, Object[] args) {
        String schedulerId = getSchedulerId(bean);
        schedulerPlusLogRepository.updateLog(schedulerId, 1, "exception");
    }

    @Override
    public void afterFinally(Object bean, Method method, Object[] args, Object result) {
        String schedulerId = getSchedulerId(bean);
        schedulerPlusTaskRepository.updateTaskStatus(schedulerId, SchedulerPlusTaskStatusEnum.DONE);
        if (result instanceof R) {
            schedulerPlusLogRepository.updateLog(schedulerId, ((R) result).getCode(), ((R) result).getMsg());
        }
    }

    @Override
    public void after(Object bean, Method method, Object[] args) {
        schedulerPlusLogRepository.updateLog(getSchedulerId(bean), 0, StringUtils.EMPTY);
    }

    private String getSchedulerId(Object bean) {
        Preconditions.checkArgument(bean instanceof SchedulerPlusExecutor, "invalid proxy bean");
        SchedulerPlusExecutor executor = (SchedulerPlusExecutor) bean;
        Preconditions.checkArgument(executor.getMetadata() != null && StringUtils.isNotBlank(executor.getMetadata().getSchedulerId()), "not found schedulerId");
        return executor.getMetadata().getSchedulerId();
    }
}
