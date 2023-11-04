package co.mgentertainment.common.redis.config;

import org.springframework.data.redis.connection.MessageListener;

/**
 * @author larry
 * @createTime 2023/11/05
 * @description RedisSubscriber
 */
public interface RedisSubscriber extends MessageListener {

    /**
     * 类型
     *
     * @return
     */
    String getType();
}
