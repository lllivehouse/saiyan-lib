package co.mgentertainment.common.schedulerplus.core;

import co.mgentertainment.common.schedulerplus.strengthen.SchedulerPlusStrength;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description SchedulerPlusCache
 */
public class SchedulerPlusCache {

    private Map<String, SchedulerPlusJob> nameToSchedulerPlusJob = new CaseInsensitiveMap<>();

    private List<SchedulerPlusStrength> strengths = new CopyOnWriteArrayList<>();

    private Map<String, ScheduledFuture> idToScheduledFuture = new ConcurrentHashMap<>();

    private Map<String, SchedulerPlusExecutor> idToSchedulerPlusExecutor = new ConcurrentHashMap<>();

    public Map<String, SchedulerPlusJob> getNameToSchedulerPlusJob() {
        return nameToSchedulerPlusJob;
    }

    public void addNameToSchedulerPlusJob(String jobName, SchedulerPlusJob schedulerPlusJob) {
        this.nameToSchedulerPlusJob.put(jobName, schedulerPlusJob);
    }

    public Map<String, ScheduledFuture> getIdToScheduledFuture() {
        return idToScheduledFuture;
    }

    public void setIdToScheduledFuture(Map<String, ScheduledFuture> idToScheduledFuture) {
        this.idToScheduledFuture = idToScheduledFuture;
    }

    public void addIdToScheduledFuture(String schedulerId, ScheduledFuture scheduledFuture) {
        this.idToScheduledFuture.put(schedulerId, scheduledFuture);
    }

    public Map<String, SchedulerPlusExecutor> getIdToSchedulerPlusExecutor() {
        return idToSchedulerPlusExecutor;
    }

    public void setIdToSchedulerPlusExecutor(Map<String, SchedulerPlusExecutor> idToSchedulerPlusExecutor) {
        this.idToSchedulerPlusExecutor = idToSchedulerPlusExecutor;
    }

    public void addIdToSchedulerPlusExecutor(String schedulerId, SchedulerPlusExecutor executor) {
        this.idToSchedulerPlusExecutor.put(schedulerId, executor);
    }

    public List<SchedulerPlusStrength> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<SchedulerPlusStrength> strengths) {
        this.strengths = strengths;
    }
}
