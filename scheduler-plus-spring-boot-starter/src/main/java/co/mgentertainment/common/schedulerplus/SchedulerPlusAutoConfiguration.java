package co.mgentertainment.common.schedulerplus;

import co.mgentertainment.common.devent.DistributedEventProvider;
import co.mgentertainment.common.devent.annonation.EnableDistributedEvent;
import co.mgentertainment.common.schedulerplus.core.SchedulerPlusCache;
import co.mgentertainment.common.schedulerplus.initial.SchedulerPlusApplicationRunner;
import co.mgentertainment.common.schedulerplus.initial.SchedulerPlusJobPostProcessor;
import co.mgentertainment.common.schedulerplus.properties.ThreadPoolTaskSchedulerProperties;
import co.mgentertainment.common.schedulerplus.strengthen.LogStrengthen;
import co.mgentertainment.common.schedulerplus.strengthen.LockStrengthen;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusLogRepository;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskRepository;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author larry
 * @createTime 2023/10/25
 * @description SchedulerPlusAutoConfiguration
 */
@Configuration
@EnableConfigurationProperties(value = {ThreadPoolTaskSchedulerProperties.class})
@EnableDistributedEvent
public class SchedulerPlusAutoConfiguration {

    @Bean(name = "threadPoolTaskScheduler")
    @ConditionalOnMissingBean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(ThreadPoolTaskSchedulerProperties threadPoolTaskSchedulerProperties) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(threadPoolTaskSchedulerProperties.getPoolSize());
        taskScheduler.setThreadNamePrefix(threadPoolTaskSchedulerProperties.getThreadNamePrefix());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(threadPoolTaskSchedulerProperties.getWaitForTasksToCompleteOnShutdown());
        taskScheduler.setAwaitTerminationSeconds(threadPoolTaskSchedulerProperties.getAwaitTerminationSeconds());
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean(name = "schedulerPlusDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource schedulerPlusDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SchedulerPlusTaskRepository schedulerPlusTaskRepository(@Qualifier("schedulerPlusDataSource") DataSource schedulerPlusDataSource) {
        return new SchedulerPlusTaskRepository(schedulerPlusDataSource);
    }

    @Bean
    public SchedulerPlusLogRepository schedulerPlusLogRepository(@Qualifier("schedulerPlusDataSource") DataSource schedulerPlusDataSource) {
        return new SchedulerPlusLogRepository(schedulerPlusDataSource);
    }

    @Bean(name = "lockRegistry")
    public LockRegistry getDefaultLockRegistry(@Qualifier("schedulerPlusDataSource") DataSource schedulerPlusDataSource) throws UnknownHostException {
        DefaultLockRepository lockRepository = new DefaultLockRepository(schedulerPlusDataSource,
                InetAddress.getLocalHost().getHostAddress() + ":" + System.getProperty("server.port"));
        // 设置分布式锁表的前缀
        lockRepository.setPrefix("scheduler_plus_");
        //设置分布式锁的过期时间,单位为毫秒,默认为15秒
        lockRepository.setTimeToLive(15_000);
        //调用方法对锁的变量进行初始化
        lockRepository.afterPropertiesSet();
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean
    public SchedulerPlusCache schedulerPlusCache() {
        return new SchedulerPlusCache();
    }

    @Bean
    @DependsOn("lockRegistry")
    @ConditionalOnProperty(prefix = "scheduler-plus.plugin", name = "lock", havingValue = "true")
    @ConditionalOnMissingBean
    public LockStrengthen lockStrengthen(@Qualifier("lockRegistry") LockRegistry lockRegistry) {
        return new LockStrengthen(lockRegistry);
    }

    @Bean
    @ConditionalOnProperty(prefix = "scheduler-plus.plugin", name = "log", havingValue = "true")
    @ConditionalOnMissingBean
    public LogStrengthen logStrengthen(SchedulerPlusTaskRepository schedulerPlusTaskRepository, SchedulerPlusLogRepository schedulerPlusLogRepository) {
        return new LogStrengthen(schedulerPlusTaskRepository, schedulerPlusLogRepository);
    }

    @Bean
    public SchedulerPlusJobPostProcessor schedulerPlusJobPostProcessor(SchedulerPlusCache schedulerPlusCache) {
        return new SchedulerPlusJobPostProcessor(schedulerPlusCache);
    }

    @Bean
    public SchedulerPlusApplicationRunner schedulerPlusApplicationRunner(ThreadPoolTaskScheduler threadPoolTaskScheduler, SchedulerPlusCache schedulerPlusCache, SchedulerPlusTaskRepository schedulerPlusTaskRepository) {
        return new SchedulerPlusApplicationRunner(threadPoolTaskScheduler, schedulerPlusCache, schedulerPlusTaskRepository);
    }

    @Bean
    public SchedulerPlusManager schedulerPlusManager(SchedulerPlusCache schedulerPlusCache, DistributedEventProvider distributedEventProvider, SchedulerPlusTaskRepository schedulerPlusTaskRepository, SchedulerPlusLogRepository schedulerPlusLogRepository) {
        return new SchedulerPlusManager(schedulerPlusCache, distributedEventProvider, schedulerPlusTaskRepository, schedulerPlusLogRepository);
    }

}
