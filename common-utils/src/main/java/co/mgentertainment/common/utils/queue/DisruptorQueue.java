package co.mgentertainment.common.utils.queue;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorDecorator
 */
public class DisruptorQueue<T> {

    private Disruptor<DisruptorEvent<T>> disruptor;
    private RingBuffer<DisruptorEvent<T>> ringBuffer;
    private WorkerPool<DisruptorEvent<T>> workerPool;

    /**
     * 创建"广播订阅模式"队列，即同一事件会被多个消费者重复并行消费
     *
     * @param bufferSize
     * @param multiProducer
     * @param consumers
     * @return
     */
    public DisruptorQueue(int bufferSize, boolean multiProducer, AbstractDisruptorConsumer<T>... consumers) {
        this.disruptor = new Disruptor(new DisruptorEventFactory(),
                bufferSize, DaemonThreadFactory.INSTANCE,
                multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new YieldingWaitStrategy());
        if (Objects.nonNull(consumers) && consumers.length > 0) {
            this.disruptor.handleEventsWith(consumers).then((disruptorEvent, l, b) -> disruptorEvent.clear());
        }
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
    }

    /**
     * 创建"发布合作订阅模式"队列，即同一事件会被多个消费者合作并行消费
     * @param bufferSize
     * @param multiProducer
     * @param executor
     * @param consumers
     */
    public DisruptorQueue(int bufferSize, boolean multiProducer, ExecutorService executor, AbstractDisruptorWorkConsumer<T>... consumers) {
        this.ringBuffer = RingBuffer.create(multiProducer ? ProducerType.MULTI : ProducerType.SINGLE, new DisruptorEventFactory<>(), bufferSize,
                new YieldingWaitStrategy());
        SequenceBarrier barriers = this.ringBuffer.newBarrier();
        this.workerPool = new WorkerPool<>(this.ringBuffer, barriers, new DisruptorEventExceptionHandler(), consumers);
        this.ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        this.workerPool.start(executor);
    }

    public void add(T t) {
        ringBuffer.publishEvent(this::translate, t);
    }

    public void addAll(List<T> list) {
        if (list != null) {
            Iterator<T> iterator = list.iterator();

            while (iterator.hasNext()) {
                T t = iterator.next();
                if (t != null) {
                    this.add(t);
                }
            }
        }
    }

    public long cursor() {
        return this.disruptor.getRingBuffer().getCursor();
    }

    public void shutdown() {
        this.disruptor.shutdown();
    }

    public Disruptor<DisruptorEvent<T>> getDisruptor() {
        return this.disruptor;
    }

    public void setDisruptor(Disruptor<DisruptorEvent<T>> disruptor) {
        this.disruptor = disruptor;
    }

    public RingBuffer<DisruptorEvent<T>> getRingBuffer() {
        return this.ringBuffer;
    }

    public void setRingBuffer(RingBuffer<DisruptorEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private void translate(DisruptorEvent event, long sequence, T t) {
        event.setEvent(t);
    }

}
