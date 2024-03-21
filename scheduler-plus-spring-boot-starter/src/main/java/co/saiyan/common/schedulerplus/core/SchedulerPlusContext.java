package co.saiyan.common.schedulerplus.core;

import co.saiyan.common.utils.GsonFactory;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description SchedulerPlusContext
 */
public class SchedulerPlusContext {

    private Map<String, Object> context = new ConcurrentHashMap<>();

    public SchedulerPlusContext() {
    }

    public SchedulerPlusContext(Map<String, Object> context) {
        this.context = context;
    }

    public SchedulerPlusContext(String contextJson) {
        if (StringUtils.isBlank(contextJson)) {
            return;
        }
        this.context = GsonFactory.getGson().fromJson(contextJson, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public void put(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object o = context.get(key);
        return GsonFactory.getGson().fromJson(GsonFactory.getGson().toJson(o), clazz);
    }

    public Map<String, Object> getContext() {
        return context;
    }
}
