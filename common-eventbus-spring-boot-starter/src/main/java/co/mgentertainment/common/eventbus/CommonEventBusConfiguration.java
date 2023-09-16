package co.mgentertainment.common.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * 事件注册
 */
@Configuration
public class CommonEventBusConfiguration implements SmartInitializingSingleton, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Bean("eventBus")
    public AsyncEventBus eventBus() {
        return EventBusFactory.getInstance().eventBus();
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, AbstractEventSubscriber> handlerMap = applicationContext.getBeansOfType(AbstractEventSubscriber.class);
        if (CollectionUtils.isEmpty(handlerMap)) {
            return;
        }
        Set<Map.Entry<String, AbstractEventSubscriber>> set = handlerMap.entrySet();
        for (Map.Entry<String, AbstractEventSubscriber> handler : set) {
            AbstractEventSubscriber instance = handler.getValue();
            if (instance != null) {
                EventBusFactory.getInstance().eventBus().register(instance);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CommonEventBusConfiguration.applicationContext = applicationContext;
    }
}
