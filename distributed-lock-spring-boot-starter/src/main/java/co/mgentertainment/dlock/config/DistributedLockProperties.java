package co.mgentertainment.dlock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author larry
 * @createTime 2023/11/10
 * @description DistributedLockProperties
 */
@Data
@ConfigurationProperties(prefix = "dlock")
public class DistributedLockProperties {
    public static final String LOCK_BY_DB = "jdbc";
    public static final String LOCK_BY_REDIS = "redis";

    private String by;

    private String redisRegistryKey = "mg.dlock";

    private Long expiredMs;
}