package co.mgentertainment.common.fileupload;

import co.mgentertainment.common.fileupload.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FileStorageProperties {

    /**
     * 默认存储平台
     */
    private String defaultPlatform = "local";
    /**
     * 缩略图后缀，例如【.min.jpg】【.png】
     */
    private String thumbnailSuffix = ".min.jpg";
    /**
     * 本地存储
     */
    private List<? extends LocalConfig> local = new ArrayList<>();
    /**
     * 本地存储
     */
    private List<? extends LocalPlusConfig> localPlus = new ArrayList<>();

    /**
     * Amazon S3
     */
    private List<? extends AmazonS3Config> amazonS3 = new ArrayList<>();

    /**
     * 基本的存储平台配置
     */
    @Data
    public static class BaseConfig {
        /**
         * 存储平台
         */
        private String platform = "";
    }

    /**
     * 本地存储
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LocalConfig extends BaseConfig {
        /**
         * 本地存储路径
         */
        private String basePath = "";
        /**
         * 访问域名
         */
        private String domain = "";
        /**
         * 其它自定义配置
         */
        private Map<String,Object> attr = new LinkedHashMap<>();
    }

    /**
     * 本地存储升级版
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class LocalPlusConfig extends BaseConfig {
        /**
         * 基础路径
         */
        private String basePath = "";
        /**
         * 存储路径，上传的文件都会存储在这个路径下面，默认“/”，注意“/”结尾
         */
        private String storagePath = "/";
        /**
         * 访问域名
         */
        private String domain = "";
        /**
         * 其它自定义配置
         */
        private Map<String,Object> attr = new LinkedHashMap<>();
    }

    /**
     * Amazon S3
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AmazonS3Config extends BaseConfig {
        private String accessKey;
        private String secretKey;
        private String region;
        private String endPoint;
        private String bucketName;
        /**
         * 访问域名
         */
        private String domain = "";
        /**
         * 基础路径
         */
        private String basePath = "";
        /**
         * 默认的 ACL，详情 {@link Constant.AmazonS3ACL}
         */
        private String defaultAcl;
        /**
         * 自动分片上传阈值，超过此大小则使用分片上传，默认 128MB
         */
        private int multipartThreshold = 128 * 1024 * 1024;
        /**
         * 自动分片上传时每个分片大小，默认 50MB
         */
        private int multipartPartSize = 50 * 1024 * 1024;
        /**
         * 其它自定义配置
         */
        private Map<String,Object> attr = new LinkedHashMap<>();
    }

    /**
     * 通用的 Client 对象池配置，详情见 {@link org.apache.commons.pool2.impl.GenericObjectPoolConfig}
     */
    @Data
    public static class CommonClientPoolConfig {
        /**
         * 取出对象前进行校验，默认开启
         */
        private Boolean testOnBorrow = true;
        /**
         * 空闲检测，默认开启
         */
        private Boolean testWhileIdle = true;
        /**
         * 最大总数量，超过此数量会进行阻塞等待，默认 16
         */
        private Integer maxTotal = 16;
        /**
         * 最大空闲数量，默认 4
         */
        private Integer maxIdle = 4;
        /**
         * 最小空闲数量，默认 1
         */
        private Integer minIdle = 1;
        /**
         * 空闲对象逐出（销毁）运行间隔时间，默认 30 秒
         */
        private Duration timeBetweenEvictionRuns = Duration.ofSeconds(30);
        /**
         * 对象空闲超过此时间将逐出（销毁），为负数则关闭此功能，默认 -1
         */
        private Duration minEvictableIdleDuration = Duration.ofMillis(-1);
        /**
         * 对象空闲超过此时间且当前对象池的空闲对象数大于最小空闲数量，将逐出（销毁），为负数则关闭此功能，默认 30 分钟
         */
        private Duration softMinEvictableIdleDuration = Duration.ofMillis(30);

        public <T> GenericObjectPoolConfig<T> toGenericObjectPoolConfig() {
            GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
            config.setTestOnBorrow(testOnBorrow);
            config.setTestWhileIdle(testWhileIdle);
            config.setMaxTotal(maxTotal);
            config.setMinIdle(minIdle);
            config.setMaxIdle(maxIdle);
            config.setTimeBetweenEvictionRuns(timeBetweenEvictionRuns);
            config.setMinEvictableIdleTime(minEvictableIdleDuration);
            config.setSoftMinEvictableIdleTime(softMinEvictableIdleDuration);
            return config;
        }
    }
}
