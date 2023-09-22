package co.mgentertainment.common.utils.queue;

/**
 * @author larry
 * @createTime 2023/2/11
 * @description DisruptorEvent
 */
public class DisruptorEvent<T> {

    private T event;

    public T getEvent() {
        return event;
    }

    public void setEvent(T event) {
        this.event = event;
    }

    public void clear() {
        this.event = null;
    }
}
