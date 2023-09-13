package co.mgentertainment.common.redis.config;

import co.mgentertainment.common.utils.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * @author larry
 * @createTime 2023/8/29
 * @description GsonRedisSerializer
 */
@Slf4j
public class GsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;

    public GsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            String json = GsonFactory.getGson().toJson(t, clazz);
            if (StringUtils.isNotBlank(json)) {
                return json.getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("GsonRedisSerializer serialize error", e);
        }
        return new byte[0];
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return GsonFactory.getGson().fromJson(str, clazz);
    }
}
