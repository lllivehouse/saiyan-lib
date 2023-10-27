package co.mgentertainment.common.schedulerplus.core;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description 调度模式
 */
public enum ScheduledModeEnum {

    /**
     * 单实例执行
     */
    SINGLE_INSTANCE(0),

    /**
     * 所有实例执行
     */
    ALL_INSTANCE(1);

    private int code;


    ScheduledModeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ScheduledModeEnum getByCode(int code) {
        return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
    }
}
