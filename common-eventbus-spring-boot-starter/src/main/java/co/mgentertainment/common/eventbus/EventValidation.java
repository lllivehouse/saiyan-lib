package co.mgentertainment.common.eventbus;

import java.util.function.Function;

/**
 * @author larry
 */
public interface EventValidation<E extends AbstractEvent> {

    Function<EventFailure, E> acceptOrReject(E event);
}
