package co.saiyan.common.eventbus;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author larry
 */
public class EventBusFactory {

    private static volatile EventBusFactory instance;
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private CountDownLatch latch = new CountDownLatch(1);
    private ThreadPoolExecutor executor;

    private AsyncEventBus eventBus = null;

    private EventBusFactory() {
        if (!this.initialized.compareAndSet(false, true)) {
            try {
                this.latch.await(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("EventBusFactory Init Failed");
            }
        } else {
            this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    Runtime.getRuntime().availableProcessors() * 4, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1000),
                    new ThreadPoolExecutor.CallerRunsPolicy());
            this.eventBus = new AsyncEventBus(executor);
        }
    }

    public static EventBusFactory getInstance() {
        if (instance == null) {
            synchronized (EventBusFactory.class) {
                if (instance == null) {
                    instance = new EventBusFactory();
                }
            }
        }
        return instance;
    }

    public AsyncEventBus eventBus() {
        return this.eventBus;
    }
}
