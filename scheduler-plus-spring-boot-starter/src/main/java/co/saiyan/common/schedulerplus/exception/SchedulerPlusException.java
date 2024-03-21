package co.saiyan.common.schedulerplus.exception;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description SchedulerPlusException
 */
public class SchedulerPlusException extends RuntimeException {
    public SchedulerPlusException() {
        super("unknown error");
    }

    public SchedulerPlusException(String message) {
        super(message);
    }

    public SchedulerPlusException(String message, Throwable cause) {
        super(message, cause);
    }
}
