package co.saiyan.common.schedulerplus.initial;

import co.saiyan.common.schedulerplus.core.SchedulerPlusCache;
import co.saiyan.common.schedulerplus.support.SchedulerPlusTaskDO;
import co.saiyan.common.schedulerplus.support.SchedulerPlusTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description SchedulerPlusApplicationRunner
 */
@Slf4j
@RequiredArgsConstructor
public class SchedulerPlusApplicationRunner implements ApplicationRunner {

    private final ThreadPoolTaskScheduler spTaskScheduler;

    private final SchedulerPlusCache schedulerPlusCache;

    private final SchedulerPlusTaskRepository schedulerPlusTaskRepository;

    @Override
    public void run(ApplicationArguments args) {
        List<SchedulerPlusTaskDO> tasks = schedulerPlusTaskRepository.listValidTask();
        for (SchedulerPlusTaskDO task : tasks) {
            schedulerPlusTaskRepository.startTask(task, schedulerPlusCache, spTaskScheduler);
        }
    }
}
