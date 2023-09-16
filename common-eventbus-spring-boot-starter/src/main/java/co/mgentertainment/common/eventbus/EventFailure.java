package co.mgentertainment.common.eventbus;

import co.mgentertainment.common.model.exception.BizException;
import lombok.AllArgsConstructor;

import java.util.Set;

/**
 * @author larry
 */
@AllArgsConstructor
public class EventFailure {
    public final Set<BizException> exceptions;
}
