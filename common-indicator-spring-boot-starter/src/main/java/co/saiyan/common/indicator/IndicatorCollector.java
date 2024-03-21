package co.saiyan.common.indicator;

import co.saiyan.common.indicator.constant.GlobalIndicatorName;
import co.saiyan.common.indicator.constant.IndicatorCategory;
import co.saiyan.common.indicator.constant.IndicatorName;
import co.saiyan.common.model.RedisKeys;
import co.saiyan.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2023/9/30
 * @description IndicatorCollector
 */
@RequiredArgsConstructor
public class IndicatorCollector {

    private final RedisService redisService;

    public Map<String, Long> getItemIndicatorCollection(IndicatorCategory type, IndicatorName name) {
        return redisService.hGetAll(getItemIndicatorKey(type, name)).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> e.getValue() instanceof Long ? (Long) e.getValue() :
                                Long.valueOf(String.valueOf(e.getValue()))));
    }

    public Long getItemIndicator(IndicatorCategory type, IndicatorName name, String itemId) {
        Object count = redisService.hGet(getItemIndicatorKey(type, name), itemId);
        return count instanceof Long ? (Long) count : Long.valueOf(String.valueOf(count));
    }

    public Map<String, Long> getGlobalIndicatorCollection(GlobalIndicatorName indicatorName) {
        return redisService.hGetAll(getGlobalIndicatorKey(indicatorName)).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> e.getValue() instanceof Long ? (Long) e.getValue() :
                                Long.valueOf(String.valueOf(e.getValue()).replace("\"", StringUtils.EMPTY))));
    }

    public static String getItemIndicatorKey(IndicatorCategory type, IndicatorName name) {
        return RedisKeys.PLAYER_INDICATOR_PREFIX + type.getCategory() + name.getValue();
    }

    public static String getGlobalIndicatorKey(GlobalIndicatorName name) {
        return RedisKeys.PLAYER_INDICATOR_PREFIX + name.getValue();
    }
}
