package co.mgentertainment.common.schedulerplus.core;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description 任务状态
 */
public enum SchedulerPlusTaskStatusEnum {

    PENDING(0, "未执行"),
    IN_PROGRESS(1, "执行中"),
    DONE(2, "已执行");

    private int code;

    private String statusName;

    SchedulerPlusTaskStatusEnum(int code, String statusName) {
        this.code = code;
        this.statusName = statusName;
    }

    public static SchedulerPlusTaskStatusEnum getByCode(int code) {
        return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getStatusName() {
        return statusName;
    }
}
