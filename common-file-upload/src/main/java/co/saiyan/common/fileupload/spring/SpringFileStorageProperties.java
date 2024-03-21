package co.saiyan.common.fileupload.spring;

import co.saiyan.common.fileupload.FileStorageProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
@ConditionalOnMissingBean(SpringFileStorageProperties.class)
@ConfigurationProperties(prefix = "spring.file-storage")
public class SpringFileStorageProperties {

    /**
     * 默认存储平台
     */
    private String defaultPlatform = "local";
    /**
     * 缩略图后缀，例如【.min.jpg】【.png】
     */
    private String thumbnailSuffix = ".min.jpg";
    /**
     * 启用 byte[] 文件包装适配器
     */
    private Boolean enableByteFileWrapper = true;
    /**
     * 启用 URI 文件包装适配器，包含 URL 和 String
     */
    private Boolean enableUriFileWrapper = true;
    /**
     * 启用 InputStream 文件包装适配器
     */
    private Boolean enableInputStreamFileWrapper = true;
    /**
     * 启用本地文件包装适配器
     */
    private Boolean enableLocalFileWrapper = true;
    /**
     * 启用 HttpServletRequest 文件包装适配器
     */
    private Boolean enableHttpServletRequestFileWrapper = true;
    /**
     * 启用 Multipart 文件包装适配器
     */
    private Boolean enableMultipartFileWrapper = true;
    /**
     * 本地存储
     */
    private List<? extends SpringLocalConfig> local = new ArrayList<>();
    /**
     * 本地存储
     */
    private List<? extends SpringLocalPlusConfig> localPlus = new ArrayList<>();

    /**
     * Amazon S3
     */
    private List<? extends SpringAmazonS3Config> amazonS3 = new ArrayList<>();

    /**
     * 转换成 FileStorageProperties ，并过滤掉没有启用的存储平台
     */
    public FileStorageProperties toFileStorageProperties() {
        FileStorageProperties properties = new FileStorageProperties();
        properties.setDefaultPlatform(defaultPlatform);
        properties.setThumbnailSuffix(thumbnailSuffix);
        properties.setLocal(local.stream().filter(SpringLocalConfig::getEnableStorage).collect(Collectors.toList()));
        properties.setLocalPlus(localPlus.stream().filter(SpringLocalPlusConfig::getEnableStorage).collect(Collectors.toList()));
        properties.setAmazonS3(amazonS3.stream().filter(SpringAmazonS3Config::getEnableStorage).collect(Collectors.toList()));
        return properties;
    }

    /**
     * 本地存储
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SpringLocalConfig extends FileStorageProperties.LocalConfig {
        /**
         * 本地存储访问路径
         */
        private String[] pathPatterns = new String[0];
        /**
         * 启用本地存储
         */
        private Boolean enableStorage = false;
        /**
         * 启用本地访问
         */
        private Boolean enableAccess = false;
    }

    /**
     * 本地存储升级版
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SpringLocalPlusConfig extends FileStorageProperties.LocalPlusConfig {
        /**
         * 本地存储访问路径
         */
        private String[] pathPatterns = new String[0];
        /**
         * 启用本地存储
         */
        private Boolean enableStorage = false;
        /**
         * 启用本地访问
         */
        private Boolean enableAccess = false;
    }

    /**
     * Amazon S3
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SpringAmazonS3Config extends FileStorageProperties.AmazonS3Config {
        /**
         * 启用存储
         */
        private Boolean enableStorage = false;
    }

}
