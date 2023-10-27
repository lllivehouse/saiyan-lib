package co.mgentertainment.common.schedulerplus.initial;

import co.mgentertainment.common.schedulerplus.core.SchedulerPlusCache;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskDO;
import co.mgentertainment.common.schedulerplus.support.SchedulerPlusTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description SchedulerPlusApplicationRunner
 */
@Slf4j
@RequiredArgsConstructor
public class SchedulerPlusApplicationRunner implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final SchedulerPlusCache schedulerPlusCache;

    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;

    @Override
    public void run(ApplicationArguments args) {
        List<SchedulerPlusTaskDO> tasks = schedulerPlusTaskRepository.listValidTask();
        for (SchedulerPlusTaskDO task : tasks) {
            schedulerPlusTaskRepository.startTask(task, schedulerPlusCache, threadPoolTaskScheduler);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
