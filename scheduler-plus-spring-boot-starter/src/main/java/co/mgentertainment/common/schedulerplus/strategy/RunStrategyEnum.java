package co.mgentertainment.common.schedulerplus.strategy;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/11/9
 * @description RunStrategyEnum
 */
public enum RunStrategyEnum {

    ONE_TIME(0),

    CRON_EXPRESSION(1);

    private int id;


    RunStrategyEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RunStrategyEnum getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == id).findFirst().orElse(RunStrategyEnum.ONE_TIME);
    }
}
