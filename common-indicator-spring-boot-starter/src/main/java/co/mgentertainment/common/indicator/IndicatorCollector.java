package co.mgentertainment.common.indicator;

import co.mgentertainment.common.indicator.constant.GlobalIndicatorName;
import co.mgentertainment.common.indicator.constant.IndicatorCategory;
import co.mgentertainment.common.indicator.constant.IndicatorName;
import co.mgentertainment.common.model.RedisKeys;
import co.mgentertainment.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;

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

    public Long getGlobalIndicator(GlobalIndicatorName indicatorName) {
        Object obj = redisService.get(getGlobalIndicatorKey(indicatorName));
        return obj instanceof Long ? (Long) obj : Long.valueOf(String.valueOf(obj));
    }

    public static String getItemIndicatorKey(IndicatorCategory type, IndicatorName name) {
        return RedisKeys.PLAYER_INDICATOR_PREFIX + type.getCategory() + name.getValue();
    }

    public static String getGlobalIndicatorKey(GlobalIndicatorName name) {
        return RedisKeys.PLAYER_INDICATOR_PREFIX + name.getValue();
    }
}
