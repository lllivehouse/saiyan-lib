package co.saiyan.common.schedulerplus.core;

import co.saiyan.common.schedulerplus.strategy.RunStrategyEnum;
import co.saiyan.common.utils.GsonFactory;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description SchedulerPlusMetadata
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class SchedulerPlusMeta<T extends SchedulerPlusJob> {

    private String schedulerId;

    private ScheduledModeEnum scheduledMode;

    private T job;

    private RunStrategyEnum strategy;

    private String strategyValue;

    private Date invokeTime;

    private Map<String, Object> contextArgs;

    private String schedulerDesc;

    private SchedulerPlusTaskStatusEnum status;

    public static Map<String, Object> parseContextArgs(String contextJson) {
        if (StringUtils.isBlank(contextJson)) {
            return Maps.newHashMap();
        }
        return GsonFactory.getGson().fromJson(contextJson, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }
}
