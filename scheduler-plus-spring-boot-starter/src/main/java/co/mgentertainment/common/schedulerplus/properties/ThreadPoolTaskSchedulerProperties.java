package co.mgentertainment.common.schedulerplus.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author larry
 * @createTime 2023/10/26
 * @description ThreadPoolTaskSchedulerProperties
 */
@ConfigurationProperties(prefix = "scheduler-plus.thread-pool")
public class ThreadPoolTaskSchedulerProperties {
    private Integer poolSize = 10;
    private String threadNamePrefix;
    private Boolean waitForTasksToCompleteOnShutdown = false;
    private Integer awaitTerminationSeconds = 0;

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public Boolean getWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(Boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public Integer getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(Integer awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }
}