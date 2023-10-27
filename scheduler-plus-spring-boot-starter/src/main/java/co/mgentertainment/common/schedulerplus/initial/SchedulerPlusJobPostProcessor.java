package co.mgentertainment.common.schedulerplus.initial;

import cn.hutool.core.lang.Pair;
import co.mgentertainment.common.schedulerplus.annontation.StrengthenOrder;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusCache;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusJob;
import co.mgentertainment.common.schedulerplus.strengthen.SchedulerPlusStrength;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description SchedulerPlusJobPostProcessor
 */
@RequiredArgsConstructor
public class SchedulerPlusJobPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final SchedulerPlusCache schedulerPlusCache;

    /**
     * 实例化bean之前的操作
     *
     * @param bean     bean实例
     * @param beanName bean的Name
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 实例化bean之后的操作
     *
     * @param bean     bean实例
     * @param beanName bean的Name
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Map<String, SchedulerPlusJob> jobBeans = applicationContext.getBeansOfType(SchedulerPlusJob.class);
        Map<String, SchedulerPlusStrength> strengthBeans = applicationContext.getBeansOfType(SchedulerPlusStrength.class);
        List<SchedulerPlusStrength> strengths = strengthBeans.values().stream().map(strengthen -> {
            StrengthenOrder annotation = strengthen.getClass().getAnnotation(StrengthenOrder.class);
            int order = Optional.ofNullable(annotation).map(StrengthenOrder::value).orElse(Integer.MAX_VALUE);
            return new Pair<>(order, strengthen);
        }).sorted(Comparator.comparing(Pair::getKey)).map(pair -> pair.getValue()).collect(Collectors.toList());
        schedulerPlusCache.setNameToSchedulerPlusJob(jobBeans);
        schedulerPlusCache.setStrengths(strengths);
        return bean;
    }

    /**
     * 获取SpringBoot的上下文
     *
     * @param applicationContext SpringBoot的上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}