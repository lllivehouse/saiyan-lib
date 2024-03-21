package co.saiyan.common.utils.queue;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorConsumer
 */
public abstract class AbstractDisruptorConsumer<T> implements EventHandler<DisruptorEvent<T>>,
        WorkHandler<DisruptorEvent<T>> {

    /**
     * 消费的实现函数
     *
     * @param t
     */
    public abstract void consume(T t);

    @Override
    public void onEvent(DisruptorEvent<T> disruptorEvent, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(disruptorEvent);
    }

    @Override
    public void onEvent(DisruptorEvent<T> disruptorEvent) throws Exception {
        this.consume(disruptorEvent.getEvent());
    }
}
