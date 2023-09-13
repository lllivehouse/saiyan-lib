package co.mgentertainment.common.devent;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2023/8/24
 * @description DistributedEvent
 */
public enum DistributedEventStore {
    INSTANCE;

    private static final Map<String, String> DEVENT_STORE = new ConcurrentHashMap<>();

    public void put(String eventKey, String value) {
        DEVENT_STORE.put(eventKey, value);
    }

    public void del(String eventKey) {
        DEVENT_STORE.remove(eventKey);
    }

    public String updatePropertiesString(String excludedKey) {
        for (Map.Entry<String, String> entry : DEVENT_STORE.entrySet()) {
            if (!entry.getKey().equals(excludedKey)) {
                DEVENT_STORE.put(entry.getKey(), StringUtils.EMPTY);
            }
        }
        return DEVENT_STORE.entrySet().stream().map(e -> e.getKey() + '=' + e.getValue()).collect(Collectors.joining("\n"));
    }

}