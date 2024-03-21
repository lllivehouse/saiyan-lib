package co.saiyan.common.redis.config;

import co.saiyan.common.utils.GsonFactory;
import com.google.gson.Gson;
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
public class GsonRedisSerializer implements RedisSerializer {

    private final Gson gson;

    public GsonRedisSerializer() {
        super();
        this.gson = GsonFactory.getGson();
    }

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        if (obj == null) {
            return new byte[0];
        }
        try {
            String json = gson.toJson(obj);
            if (StringUtils.isNotBlank(json)) {
                return json.getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("GsonRedisSerializer serialize error", e);
        }
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(str, Object.class);
    }
}
