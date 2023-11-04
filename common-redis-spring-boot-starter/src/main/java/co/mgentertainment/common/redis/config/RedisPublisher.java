package co.mgentertainment.common.redis.config;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @author larry
 * @createTime 2023/11/05
 * @description RedisPublisher
 */
@RequiredArgsConstructor
public class RedisPublisher implements MessagePublisher {

    private final RedisTemplate redisTemplate;

    @Override
    public void publish(String channelName, Object message) {
        Preconditions.checkArgument(StringUtils.isNotBlank(channelName) && Objects.nonNull(message),
                "invalid channelName or message");
        this.redisTemplate.convertAndSend(channelName, message);
    }
}
