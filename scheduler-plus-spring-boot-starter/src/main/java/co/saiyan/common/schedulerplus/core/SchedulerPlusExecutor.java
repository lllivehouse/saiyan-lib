package co.saiyan.common.schedulerplus.core;

import co.saiyan.common.model.R;

import java.util.Map;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description SchedulerPlusExecutor
 */
public class SchedulerPlusExecutor {

    private SchedulerPlusMeta metadata;

    public SchedulerPlusExecutor(SchedulerPlusMeta metadata) {
        this.metadata = metadata;
    }

    public R<Boolean> invoke() {
        SchedulerPlusJob job = metadata.getJob();
        if (job == null) {
            throw new IllegalArgumentException("job can not be null");
        }
        Map<String, Object> contextArgs = metadata.getContextArgs();
        SchedulerPlusContext context = new SchedulerPlusContext(contextArgs);
        return job.execute(context);
    }

    public SchedulerPlusMeta getMetadata() {
        return metadata;
    }

    public void setTaskStatus(SchedulerPlusTaskStatusEnum status) {
        metadata.setStatus(status);
    }
}
