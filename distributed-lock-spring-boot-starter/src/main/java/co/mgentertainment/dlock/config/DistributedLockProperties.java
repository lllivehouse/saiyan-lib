package co.mgentertainment.dlock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author larry
 * @createTime 2023/11/10
 * @description DistributedLockProperties
 */
@ConfigurationProperties(prefix = "dlock")
@Data
public class DistributedLockProperties {
    private static final String LOCK_BY_DB = "jdbc";
    private static final String LOCK_BY_REDIS = "redis";

    private String lockType = LOCK_BY_DB;

    private String lockKey;

    private Long expiredMs;
}