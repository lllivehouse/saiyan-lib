package co.mgentertainment.common.utils.queue;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.Objects;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorDecoratorFactory
 */
public class DisruptorQueueFactory {

    /**
     * 创建"发布订阅模式"队列，即同一事件会被多个消费者并行消费
     *
     * @param queueSize
     * @param multiProducer
     * @param consumers
     * @param <T>
     * @return
     */
    public static <T> DisruptorQueue<T> createPubSubQueue(int queueSize, boolean multiProducer,
                                                          AbstractDisruptorConsumer<T>... consumers) {
        Disruptor<DisruptorEvent<T>> disruptor = new Disruptor(new DisruptorEventFactory(),
                queueSize, DaemonThreadFactory.INSTANCE,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new YieldingWaitStrategy());
        if (Objects.nonNull(consumers) && consumers.length > 0) {
            disruptor.handleEventsWith(consumers).then((disruptorEvent, l, b) -> disruptorEvent.clear());
        }
        return new DisruptorQueue(disruptor);
    }

    public static <T> void shutdown(DisruptorQueue<T> disruptorQueue) {
        disruptorQueue.shutdown();
    }
}
