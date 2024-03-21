package co.saiyan.common.schedulerplus.core;

import co.saiyan.common.model.R;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description SchedulerPlusJob
 */
public abstract class SchedulerPlusJob {

    public abstract R<Boolean> execute(SchedulerPlusContext context);
}
