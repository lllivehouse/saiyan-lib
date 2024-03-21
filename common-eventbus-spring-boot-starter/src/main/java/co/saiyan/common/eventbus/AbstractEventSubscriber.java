package co.saiyan.common.eventbus;

/**
 * @author larry
 * @createTime 14/08/2023
 * @description AbstractEventSubscriber
 */
public abstract class AbstractEventSubscriber<E extends AbstractEvent> {
    public abstract void subscribe(E event);
}
