package co.mgentertainment.common.utils.queue;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Iterator;
import java.util.List;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorDecorator
 */
public class DisruptorQueue<T> {

    private Disruptor<DisruptorEvent<T>> disruptor;
    private RingBuffer<DisruptorEvent<T>> ringBuffer;

    public DisruptorQueue(Disruptor<DisruptorEvent<T>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
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
