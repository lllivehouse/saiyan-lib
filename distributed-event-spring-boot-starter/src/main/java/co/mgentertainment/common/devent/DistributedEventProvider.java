package co.mgentertainment.common.devent;

import co.mgentertainment.common.devent.annonation.DistributedEventListener;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

/**
 * @author larry
 * @createTime 2023/8/24
 * @description DistributedEvent
 */
@RequiredArgsConstructor
public class DistributedEventProvider<T> implements ApplicationContextAware, SmartInitializingSingleton {
    private static final String DATA_ID = "_distributed_event.properties";
    private static final String SEPERATOR = "<!>";

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private final Environment environment;

    private ApplicationContext applicationContext;

    private Map<String, DistributedEventCallback> listeners;

    private ConfigService configService;

    private String appDataId;

    @Override
    public void afterSingletonsInstantiated() {
        initListener();
        try {
            this.appDataId = environment.getProperty("spring.application.name") + DATA_ID;
            initConfigServer(appDataId, DEFAULT_GROUP);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void initListener() {
        Map<String, Object> listenerBeans = applicationContext.getBeansWithAnnotation(DistributedEventListener.class);
        this.listeners = listenerBeans.entrySet().stream().filter(e -> applicationContext.isTypeMatch(e.getKey(), DistributedEventCallback.class))
                .collect(Collectors.toMap(
                        e -> AnnotationUtils.findAnnotation(e.getValue().getClass(), DistributedEventListener.class).eventKey(),
                        e -> (DistributedEventCallback) e.getValue()));
    }

    private void initConfigServer(String dataId, String group) throws NacosException {
        try {
            this.configService = NacosFactory.createConfigService(nacosDiscoveryProperties.getNacosProperties());
        } catch (NacosException e) {
            throw new RuntimeException("fail to create nacos config server", e);
        }
        this.configService.addListener(dataId, group, new PropertiesListener() {
            @Override
            public Executor getExecutor() {
                return Executors.newCachedThreadPool(r -> {
                    Thread t = new Thread(r);
                    t.setName("devent-t");
                    // 设置线程为守护线程
                    t.setDaemon(true);
                    return t;
                });
            }

            @Override
            public void innerReceive(Properties properties) {
                Enumeration<?> enumeration = properties.propertyNames();
                while (enumeration.hasMoreElements()) {
                    Object o = enumeration.nextElement();
                    String key = o.toString();
                    String value = properties.getProperty(key);
                    if (StringUtils.isNotBlank(value)) {
                        DistributedEventCallback callback = listeners.get(key);
                        if (callback != null) {
                            value = StringUtils.substringBefore(value, SEPERATOR);
                            callback.onComplete(value);
                        }
                    }

                }
            }
        });
    }

    public void fire(String eventKey, String value) throws NacosException {
        if (StringUtils.isAnyBlank(eventKey, value)) {
            return;
        }
        String newValue = String.format("%s%s%d", value, SEPERATOR, System.currentTimeMillis());
        DistributedEventStore.INSTANCE.put(eventKey, newValue);
        String body = DistributedEventStore.INSTANCE.updatePropertiesString(eventKey);
        configService.publishConfig(this.appDataId, DEFAULT_GROUP, body);
    }

    public void clear() throws NacosException {
        configService.removeConfig(this.appDataId, DEFAULT_GROUP);
    }
}