package co.mgentertainment.common.schedulerplus.support;

import co.mgentertainment.common.schedulerplus.core.SchedulerPlusJob;
import co.mgentertainment.common.schedulerplus.strategy.RunStrategyEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SchedulerPlusTaskItem implements Serializable {

    private static final long serialVersionUID = 8705164441905158105L;

    private String schedulerId;

    private Integer scheduledMode;

    private Class<? extends SchedulerPlusJob> jobClass;

    private RunStrategyEnum strategy;

    private String strategyValue;

    private String taskArgs;

    private String taskDesc;

    private Integer taskStatus;

}