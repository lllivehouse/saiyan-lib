package co.mgentertainment.common.eventbus;

import java.util.function.Function;

/**
 * @author larry
 */
public interface EventValidation<E extends BaseEvent> {

    Function<EventFailure, E> acceptOrReject(E event);
}
