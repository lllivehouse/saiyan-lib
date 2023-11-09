package co.mgentertainment.common.schedulerplus.support;

import co.mgentertainment.common.schedulerplus.core.*;
import co.mgentertainment.common.schedulerplus.proxy.SchedulerPlusExecutorProxy;
import co.mgentertainment.common.schedulerplus.strategy.RunStrategyEnum;
import co.mgentertainment.common.schedulerplus.strategy.ScheduledFutureFactory;
import co.mgentertainment.common.schedulerplus.strengthen.SchedulerPlusStrength;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @author larry
 * @createTime 26/10/2023
 * @description SchedulerPlusTaskRepository
 */
@Slf4j
public class SchedulerPlusTaskRepository implements InitializingBean {

    private static final String SQL_QUERY_TASK = "select scheduler_id, scheduled_mode, job_name, run_strategy_id, run_strategy_value, task_args, task_desc, task_status, create_time, updated_time, deleted from scheduler_plus_task where deleted=0";
    private static final String SQL_INSERT_TASK = "insert into scheduler_plus_task(scheduler_id, scheduled_mode, job_name, run_strategy_id, run_strategy_value, task_args, task_desc, task_status) values(?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_TASK_STRATEGY = "update scheduler_plus_task set run_strategy_id=?, run_strategy_value=?, updated_time=now() where scheduler_id=?";
    private static final String SQL_UPDATE_TASK_STATUS = "update scheduler_plus_task set task_status=?, updated_time=now() where scheduler_id=?";
    private static final String SQL_REMOVE_TASK = "update scheduler_plus_task set deleted=1, updated_time=now() where scheduler_id=?";

    private JdbcTemplate jdbcTemplate;

    private final DataSource schedulerPlusDataSource;

    public SchedulerPlusTaskRepository(@Qualifier("schedulerPlusDataSource") DataSource schedulerPlusDataSource) {
        this.schedulerPlusDataSource = schedulerPlusDataSource;
    }

    @Override
    public void afterPropertiesSet() {
        this.jdbcTemplate = new JdbcTemplate(schedulerPlusDataSource);
    }

    public List<SchedulerPlusTaskDO> listValidTask() {
        List<SchedulerPlusTaskDO> tasks = jdbcTemplate.query(SQL_QUERY_TASK, (rs, rownumber) -> {
            SchedulerPlusTaskDO task = new SchedulerPlusTaskDO();
            task.setSchedulerId(rs.getString(1));
            task.setScheduledMode(rs.getInt(2));
            task.setJobName(rs.getString(3));
            task.setRunStrategyId(rs.getInt(4));
            task.setRunStrategyValue(rs.getString(5));
            task.setTaskArgs(rs.getString(6));
            task.setTaskDesc(rs.getString(7));
            task.setTaskStatus(rs.getInt(8));
            task.setCreateTime(rs.getDate(9));
            task.setUpdatedTime(rs.getDate(10));
            task.setDeleted(rs.getByte(11));
            return task;
        });
        // 过滤非法和过期的任务
        return tasks.stream().filter(t -> !StrategyValueUtils.isStrategyValueInvalidOrExpired(RunStrategyEnum.getById(t.getRunStrategyId()), t.getRunStrategyValue()))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean createTask(SchedulerPlusTaskDO task) {
        if (StrategyValueUtils.isStrategyValueInvalidOrExpired(RunStrategyEnum.getById(task.getRunStrategyId()), task.getRunStrategyValue())) {
            throw new IllegalArgumentException("strategy value is invalid or expired");
        }
        return jdbcTemplate.update(SQL_INSERT_TASK,
                SchedulerIdGenerator.generate(),
                task.getScheduledMode(),
                task.getJobName(),
                task.getRunStrategyId(),
                task.getRunStrategyValue(),
                task.getTaskArgs(),
                task.getTaskDesc(),
                task.getTaskStatus()) > 0;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean updateTaskStrategy(String schedulerId, RunStrategyEnum strategyEnum, String strategyValue) {
        Object[] args = new Object[]{strategyEnum.getId(), strategyValue, schedulerId};
        return jdbcTemplate.update(SQL_UPDATE_TASK_STRATEGY, args) > 0;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean updateTaskStatus(String schedulerId, SchedulerPlusTaskStatusEnum status) {
        Object[] args = new Object[]{status.getCode(), schedulerId};
        return jdbcTemplate.update(SQL_UPDATE_TASK_STATUS, args) > 0;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public boolean removeTask(String schedulerId) {
        Object[] args = new Object[]{schedulerId};
        return jdbcTemplate.update(SQL_REMOVE_TASK, args) > 0;
    }

    /**
     * 开启任务
     *
     * @param task
     * @param cache
     * @param threadPoolTaskScheduler
     */
    public void startTask(SchedulerPlusTaskDO task, SchedulerPlusCache cache, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        String jobName = task.getJobName();
        Map<String, SchedulerPlusJob> jobs = cache.getNameToSchedulerPlusJob();
        SchedulerPlusJob schedulerPlusJob = jobs.get(jobName);
        List<SchedulerPlusStrength> strengths = cache.getStrengths();
        SchedulerPlusMeta<SchedulerPlusJob> schedulerPlusMeta = extractSchedulerPlusMeta(task, schedulerPlusJob);
        SchedulerPlusExecutor proxy = new SchedulerPlusExecutorProxy(strengths).createProxy(schedulerPlusMeta);
        // 代理添加到缓存
        cache.addIdToSchedulerPlusExecutor(task.getSchedulerId(), proxy);
        // 任务启动
        try {
            ScheduledFuture<?> scheduledFuture = ScheduledFutureFactory.create(threadPoolTaskScheduler, proxy);
            cache.addIdToScheduledFuture(task.getSchedulerId(), scheduledFuture);
        } catch (Exception e) {
            log.error("任务{}启动失败，错误信息：{}", jobName, e.getLocalizedMessage());
        }
    }

    private SchedulerPlusMeta<SchedulerPlusJob> extractSchedulerPlusMeta(SchedulerPlusTaskDO task, SchedulerPlusJob job) {
        return SchedulerPlusMeta.builder()
                .schedulerId(task.getSchedulerId())
                .scheduledMode(ScheduledModeEnum.getByCode(task.getScheduledMode()))
                .contextArgs(SchedulerPlusMeta.parseContextArgs(task.getTaskArgs()))
                .job(job)
                .strategy(RunStrategyEnum.getById(task.getRunStrategyId()))
                .strategyValue(task.getRunStrategyValue())
                .schedulerDesc(task.getTaskDesc())
                .status(SchedulerPlusTaskStatusEnum.getByCode(task.getTaskStatus()))
                .build();
    }
}
