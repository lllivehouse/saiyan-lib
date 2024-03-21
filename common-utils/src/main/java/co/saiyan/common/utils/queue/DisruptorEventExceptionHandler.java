package co.saiyan.common.utils.queue;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author larry
 * @createTime 2023/9/23
 * @description DisruptorEventExceptionHandler
 */
@Slf4j
public class DisruptorEventExceptionHandler implements ExceptionHandler {
    @Override
    public void handleEventException(Throwable throwable, long l, Object o) {
        log.error("DisruptorEvent handleEvent Exception:", throwable);
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.error("DisruptorEvent handleOnStart Exception:", throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.error("DisruptorEvent handleOnShutdown Exception:", throwable);
    }
}
