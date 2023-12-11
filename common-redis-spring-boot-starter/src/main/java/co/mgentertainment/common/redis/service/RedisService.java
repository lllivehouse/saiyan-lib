package co.mgentertainment.common.redis.service;

import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author larry
 * @createTime 2023/8/29
 * @description RedisService
 */
public interface RedisService {

    /**
     * redis 批量事务操作
     *
     * @param callback
     * @param <T>
     * @return
     */
    <T> T execute(SessionCallback<T> callback) throws RuntimeException;

    /**
     * 保存属性
     */
    void set(String key, Object value, long time) throws RuntimeException;

    /**
     * 保存属性
     */
    void set(String key, Object value) throws RuntimeException;

    /**
     * 获取属性
     */
    Object get(String key) throws RuntimeException;

    /**
     * 删除属性
     */
    Boolean del(String key) throws RuntimeException;

    /**
     * 批量删除属性
     */
    Long del(List<String> keys) throws RuntimeException;

    /**
     * 设置过期时间
     */
    Boolean expire(String key, long time) throws RuntimeException;

    /**
     * 获取过期时间
     */
    Long getExpire(String key) throws RuntimeException;

    /**
     * 判断是否有该属性
     */
    Boolean hasKey(String key) throws RuntimeException;

    /**
     * 按delta递增
     */
    Long incr(String key, long delta) throws RuntimeException;

    /**
     * 按delta递减
     */
    Long decr(String key, long delta) throws RuntimeException;

    /**
     * 获取Hash结构中的属性
     */
    Object hGet(String key, String hashKey) throws RuntimeException;

    /**
     * 判断hkey是否存在
     */
    Boolean hHashKey(String key, String hashKey) throws RuntimeException;

    /**
     * 获取Hash结构中的所有键值对
     * @param key
     * @return
     * @throws RuntimeException
     */
    Map<Object, Object> hEntries(String key) throws RuntimeException;

    /**
     * 向Hash结构中放入一个属性
     */
    Boolean hSet(String key, String hashKey, Object value, long time) throws RuntimeException;

    /**
     * 向Hash结构中放入一个属性
     */
    void hSet(String key, String hashKey, Object value) throws RuntimeException;

    /**
     * 直接获取整个Hash结构
     */
    Map<Object, Object> hGetAll(String key) throws RuntimeException;

    /**
     * 直接设置整个Hash结构
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time) throws RuntimeException;

    /**
     * 直接设置整个Hash结构
     */
    void hSetAll(String key, Map<String, Object> map) throws RuntimeException;

    /**
     * 删除Hash结构中的属性
     */
    void hDel(String key, Object... hashKey) throws RuntimeException;

    /**
     * 判断Hash结构中是否有该属性
     */
    Boolean hHasKey(String key, String hashKey) throws RuntimeException;

    /**
     * Hash结构中属性递增
     */
    Long hIncr(String key, String hashKey, Long delta) throws RuntimeException;

    /**
     * Hash结构中属性递减
     */
    Long hDecr(String key, String hashKey, Long delta) throws RuntimeException;

    /**
     * 获取Set结构
     */
    Set<Object> sMembers(String key) throws RuntimeException;

    /**
     * 向Set结构中添加属性
     */
    Long sAdd(String key, Object... values) throws RuntimeException;

    /**
     * 向Set结构中添加属性
     */
    Long sAdd(String key, long time, Object... values) throws RuntimeException;

    /**
     * 是否为Set中的属性
     */
    Boolean sIsMember(String key, Object value) throws RuntimeException;

    /**
     * 获取Set结构的长度
     */
    Long sSize(String key) throws RuntimeException;

    /**
     * 删除Set结构中的属性
     */
    Long sRemove(String key, Object... values) throws RuntimeException;

    /**
     * 获取List结构中的属性
     */
    List<Object> lRange(String key, long start, long end) throws RuntimeException;

    /**
     * 获取List结构的长度
     */
    Long lSize(String key) throws RuntimeException;

    /**
     * 根据索引获取List中的属性
     */
    Object lIndex(String key, long index) throws RuntimeException;

    /**
     * 向List结构中添加属性
     */
    Long lPush(String key, Object value) throws RuntimeException;

    /**
     * 向List结构中添加属性
     */
    Long lPush(String key, Object value, long time) throws RuntimeException;

    /**
     * 向List结构中批量添加属性
     */
    Long lPushAll(String key, Object... values) throws RuntimeException;

    /**
     * 向List结构中批量添加属性
     */
    Long lPushAll(String key, Long time, Object... values) throws RuntimeException;

    /**
     * 从List结构中移除属性
     */
    Long lRemove(String key, long count, Object value) throws RuntimeException;

    /**
     * zset 添加元素
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    Boolean zAdd(String key, Object value, long score);

    /**
     * 批量添加
     *
     * @param key
     * @param tuples
     * @return
     */
    Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> tuples);

    /**
     * zrange 获取元素
     *
     * @param key
     * @param offset
     * @param count
     * @return
     */
    <T> Set<T> zRange(String key, long offset, long count);

    /**
     * zrange 获取范围值内的元素
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<Object> zRangeByScope(String key, long min, long max, long offset, long count);

    /**
     * zrange 获取范围值内的元素
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    Set<Object> zRangeByScope(String key, Long min, Long max);

    /**
     * zset 分数倒序获取元素
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    Set<Object> zReverseRangeByScope(String key, long min, long max, long offset, long count);

    /**
     * zset 按照偏移量删除元素
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    Long zRemoveRange(String key, long startOffset, long endOffset);

    /**
     * zset 按照范围删除元素
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    Long zRemoveRangeByScope(String key, long min, long max);

    /**
     * zset 弹出最大元素
     *
     * @param key
     * @return
     */
    Object zPopMax(String key);

    /**
     * zset 弹出最小元素
     *
     * @param key
     * @return
     */
    Object zPopMin(String key);
}
