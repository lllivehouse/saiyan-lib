package co.mgentertainment.common.utils.queue;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorDecoratorFactory
 */
public class DisruptorQueueFactory {

    /**
     * 创建"广播订阅模式"队列，即同一事件会被多个消费者重复并行消费
     *
     * @param bufferSize
     * @param multiProducer
     * @param consumers
     * @param <T>
     * @return
     */
    public static <T> DisruptorQueue<T> createPubSubQueue(int bufferSize, boolean multiProducer,
                                                          AbstractDisruptorConsumer<T>... consumers) {
        Disruptor<DisruptorEvent<T>> disruptor = new Disruptor(new DisruptorEventFactory(),
                bufferSize, DaemonThreadFactory.INSTANCE,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new YieldingWaitStrategy());
        if (Objects.nonNull(consumers) && consumers.length > 0) {
            disruptor.handleEventsWith(consumers).then((disruptorEvent, l, b) -> disruptorEvent.clear());
        }
        return new DisruptorQueue(disruptor);
    }

    /**
     * 创建"发布合作订阅模式"队列，即同一事件会被多个消费者合作并行消费
     *
     * @param bufferSize
     * @param workerSize
     * @param multiProducer
     * @param consumers
     * @param <T>
     * @return
     */
    public static <T> DisruptorQueue<T> createWorkPoolQueue(int bufferSize, int workerSize, boolean multiProducer,
                                                            AbstractDisruptorWorkConsumer<T>... consumers) {
        Disruptor<DisruptorEvent<T>> disruptor = new Disruptor(new DisruptorEventFactory(), bufferSize,
                new ThreadPoolExecutor(workerSize, workerSize, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
                    private int counter = 0;

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "DisruptorWorker-" + counter++);
                    }
                }), multiProducer ? ProducerType.MULTI : ProducerType.SINGLE, new YieldingWaitStrategy());
        if (Objects.nonNull(consumers) && consumers.length > 0) {
            disruptor.handleEventsWithWorkerPool(consumers).then((disruptorEvent, l, b) -> disruptorEvent.clear());
        }
        return new DisruptorQueue(disruptor);
    }

    public static <T> void shutdown(DisruptorQueue<T> disruptorQueue) {
        disruptorQueue.shutdown();
    }
}
