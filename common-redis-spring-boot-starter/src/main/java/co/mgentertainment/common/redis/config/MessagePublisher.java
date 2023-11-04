package co.mgentertainment.common.redis.config;

/**
 * @author larry
 * @createTime 2023/11/05
 * @description MessagePublisher
 */
public interface MessagePublisher {

    /**
     * i向主题通道发布消息
     *
     * @param channelName
     * @param message
     */
    void publish(String channelName, Object message);

}