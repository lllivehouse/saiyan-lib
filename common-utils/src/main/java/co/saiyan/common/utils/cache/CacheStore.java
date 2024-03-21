package co.saiyan.common.utils.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author larry
 * @description CacheStore
 */
public class CacheStore<K, V> {
    private static final int DEFAULT_CAPACITY = Integer.MAX_VALUE;
    private static final int DEFAULT_EXPIRED_SECOND = 3600 * 24;

    private final Cache<K, V> cache;

    /**
     * constructor
     *
     * @param capacity
     * @param expireSec never expiration if null or 0
     */
    private CacheStore(Integer capacity, Integer expireSec) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder().maximumSize(capacity).weakValues();
        if (Objects.nonNull(expireSec) && expireSec > 0) {
            cacheBuilder.expireAfterAccess(expireSec, TimeUnit.SECONDS).expireAfterWrite(expireSec, TimeUnit.SECONDS);
        }
        this.cache = cacheBuilder.build();
    }

    public void put(K key, V value) {
        if (Objects.isNull(key) || Objects.isNull(value)) {
            throw new RuntimeException("fail to put cache.Both key and value should not be null.");
        }
        this.cache.put(key, value);
    }

    public void putAll(Map<K, V> values) {
        if (Objects.isNull(values) ||
                values.entrySet().stream().anyMatch(e -> Objects.isNull(e.getKey()) || Objects.isNull(e.getValue()))) {
            throw new RuntimeException("fail to putAll cache.Map's kv should not be null.");
        }
        this.cache.putAll(values);
    }

    public V get(K key) {
        if (Objects.isNull(key)) {
            throw new RuntimeException("fail to get cache.Key should not be null.");
        }
        return this.cache.getIfPresent(key);
    }

    public ConcurrentMap<K, V> getAll() {
        return this.cache.asMap();
    }

    public void remove(K key) {
        if (Objects.isNull(key)) {
            throw new RuntimeException("fail to remove cache.Key should not be null.");
        }
        this.cache.invalidate(key);
    }

    public void removeAll() {
        this.cache.invalidateAll();
    }

    public long size() {
        return this.cache.size();
    }

    public static class Builder {
        private int capacity;
        private int expireSec;

        public Builder setCapacity(Integer capacity) {
            if (Objects.isNull(capacity)) {
                this.capacity = DEFAULT_CAPACITY;
            } else {
                this.capacity = capacity;
            }
            return this;
        }

        public Builder setExpireSec(Integer expireSec) {
            if (Objects.isNull(expireSec)) {
                this.expireSec = DEFAULT_EXPIRED_SECOND;
            } else {
                this.expireSec = expireSec;
            }
            return this;
        }

        public CacheStore build() {
            return new CacheStore(this.capacity, this.expireSec);
        }
    }

}
