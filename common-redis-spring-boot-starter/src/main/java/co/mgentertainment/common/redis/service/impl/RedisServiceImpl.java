package co.mgentertainment.common.redis.service.impl;

import co.mgentertainment.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2023/8/29
 * @description RedisService
 */
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> T execute(SessionCallback<T> callback) throws RuntimeException {
        return redisTemplate.execute(callback);
    }

    @Override
    public void set(String key, Object value, long time) throws RuntimeException {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) throws RuntimeException {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) throws RuntimeException {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean del(String key) throws RuntimeException {
        return redisTemplate.delete(key);
    }

    @Override
    public Long del(List<String> keys) throws RuntimeException {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean expire(String key, long time) throws RuntimeException {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) throws RuntimeException {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) throws RuntimeException {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long incr(String key, long delta) throws RuntimeException {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long decr(String key, long delta) throws RuntimeException {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) throws RuntimeException {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Set<String> hKeys(String key) throws RuntimeException {
        return redisTemplate.opsForHash().keys(key).stream().map(k -> String.valueOf(k)).collect(Collectors.toSet());
    }

    @Override
    public Map<Object, Object> hEntries(String key) throws RuntimeException {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) throws RuntimeException {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) throws RuntimeException {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) throws RuntimeException {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) throws RuntimeException {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, Object> map) throws RuntimeException {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void hDel(String key, Object... hashKey) throws RuntimeException {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) throws RuntimeException {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) throws RuntimeException {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) throws RuntimeException {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) throws RuntimeException {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sAdd(String key, Object... values) throws RuntimeException {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) throws RuntimeException {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) throws RuntimeException {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long sSize(String key) throws RuntimeException {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Long sRemove(String key, Object... values) throws RuntimeException {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) throws RuntimeException {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Long lSize(String key) throws RuntimeException {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Object lIndex(String key, long index) throws RuntimeException {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Long lPush(String key, Object value) throws RuntimeException {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long lPush(String key, Object value, long time) throws RuntimeException {
        Long index = redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) throws RuntimeException {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) throws RuntimeException {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) throws RuntimeException {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public Boolean zAdd(String key, Object value, long score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    @Override
    public Set<Object> zRange(String key, long startOffset, long endOffset) {
        return redisTemplate.opsForZSet().range(key, startOffset, endOffset);
    }

    @Override
    public Set<Object> zRangeByScope(String key, long min, long max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, new Long(min).doubleValue(), new Long(max).doubleValue(), offset, count);
    }

    @Override
    public Set<Object> zRangeByScope(String key, Long min, Long max) {
        return redisTemplate.opsForZSet().rangeByScore(key, new Long(min).doubleValue(), new Long(max).doubleValue());
    }

    @Override
    public Set<Object> zReverseRangeByScope(String key, long min, long max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, new Long(min).doubleValue(), new Long(max).doubleValue(), offset, count);
    }

    @Override
    public Object zPopMax(String key) {
        return redisTemplate.opsForZSet().popMax(key).getValue();
    }

    @Override
    public Object zPopMin(String key) {
        return redisTemplate.opsForZSet().popMin(key).getValue();
    }

    @Override
    public Long zRemoveRange(String key, long startOffset, long endOffset) {
        return redisTemplate.opsForZSet().removeRange(key, startOffset, endOffset);
    }

    @Override
    public Long zRemoveRangeByScope(String key, long min, long max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, new Long(min).doubleValue(), new Long(max).doubleValue());
    }
}
