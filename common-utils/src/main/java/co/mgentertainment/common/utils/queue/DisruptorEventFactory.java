package co.mgentertainment.common.utils.queue;

import com.lmax.disruptor.EventFactory;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorEventFactory
 */
public class DisruptorEventFactory<T> implements EventFactory<DisruptorEvent<T>> {

    @Override
    public DisruptorEvent newInstance() {
        return new DisruptorEvent();
    }
}
