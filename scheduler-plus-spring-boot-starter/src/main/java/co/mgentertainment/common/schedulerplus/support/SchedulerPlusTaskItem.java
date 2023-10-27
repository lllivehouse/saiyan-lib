package co.mgentertainment.common.schedulerplus.support;

import lombok.Data;

import java.io.Serializable;

@Data
public class SchedulerPlusTaskItem implements Serializable {

    private static final long serialVersionUID = 8705164441905158105L;

    private String schedulerId;

    private Integer scheduledMode;

    private String jobName;

    private String cronExpression;

    private String taskArgs;

    private String taskDesc;

    private Integer taskStatus;

}