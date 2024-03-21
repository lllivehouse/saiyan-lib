package co.saiyan.common.utils.queue;

import com.lmax.disruptor.WorkHandler;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description AbstractDisruptorWorkConsumer
 */
public abstract class AbstractDisruptorWorkConsumer<T> implements WorkHandler<DisruptorEvent<T>> {

    /**
     * 消费的实现函数
     *
     * @param t
     */
    public abstract void consume(T t);

    @Override
    public void onEvent(DisruptorEvent<T> disruptorEvent) throws Exception {
        this.consume(disruptorEvent.getEvent());
    }
}
