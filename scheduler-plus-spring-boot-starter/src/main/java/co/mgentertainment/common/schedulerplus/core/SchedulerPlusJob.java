package co.mgentertainment.common.schedulerplus.core;

import co.mgentertainment.common.model.R;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description SchedulerPlusJob
 */
public abstract class SchedulerPlusJob {

    public abstract R<Boolean> execute(SchedulerPlusContext context);
}
