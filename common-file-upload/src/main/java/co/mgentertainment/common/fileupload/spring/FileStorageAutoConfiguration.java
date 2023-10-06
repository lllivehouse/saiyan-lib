package co.mgentertainment.common.fileupload.spring;

import co.mgentertainment.common.fileupload.FileStorageService;
import co.mgentertainment.common.fileupload.FileStorageServiceBuilder;
import co.mgentertainment.common.fileupload.aspect.FileStorageAspect;
import co.mgentertainment.common.fileupload.file.FileWrapperAdapter;
import co.mgentertainment.common.fileupload.platform.FileStorage;
import co.mgentertainment.common.fileupload.platform.FileStorageClientFactory;
import co.mgentertainment.common.fileupload.recorder.DefaultFileRecorder;
import co.mgentertainment.common.fileupload.recorder.FileRecorder;
import co.mgentertainment.common.fileupload.tika.ContentTypeDetect;
import co.mgentertainment.common.fileupload.tika.DefaultTikaFactory;
import co.mgentertainment.common.fileupload.tika.TikaContentTypeDetect;
import co.mgentertainment.common.fileupload.tika.TikaFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@ConditionalOnMissingBean(FileStorageService.class)
public class FileStorageAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private SpringFileStorageProperties properties;
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 配置本地存储的访问地址
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for (SpringFileStorageProperties.SpringLocalConfig local : properties.getLocal()) {
            if (local.getEnableStorage() && local.getEnableAccess()) {
                registry.addResourceHandler(local.getPathPatterns()).addResourceLocations("file:" + local.getBasePath());
            }
        }
        for (SpringFileStorageProperties.SpringLocalPlusConfig local : properties.getLocalPlus()) {
            if (local.getEnableStorage() && local.getEnableAccess()) {
                registry.addResourceHandler(local.getPathPatterns()).addResourceLocations("file:" + local.getStoragePath());
            }
        }
    }

    /**
     * 当没有找到 FileRecorder 时使用默认的 FileRecorder
     */
    @Bean
    @ConditionalOnMissingBean(FileRecorder.class)
    public FileRecorder fileRecorder() {
        log.warn("没有找到 FileRecorder 的实现类，文件上传之外的部分功能无法正常使用，必须实现该接口才能使用完整功能！");
        return new DefaultFileRecorder();
    }

    /**
     * Tika 工厂类型，用于识别上传的文件的 MINE
     */
    @Bean
    @ConditionalOnMissingBean(TikaFactory.class)
    public TikaFactory tikaFactory() {
        return new DefaultTikaFactory();
    }

    /**
     * 识别文件的 MIME 类型
     */
    @Bean
    @ConditionalOnMissingBean(ContentTypeDetect.class)
    public ContentTypeDetect contentTypeDetect(TikaFactory tikaFactory) {
        return new TikaContentTypeDetect(tikaFactory);
    }

    /**
     * 文件存储服务
     */
    @Bean(destroyMethod = "destroy")
    public FileStorageService fileStorageService(FileRecorder fileRecorder,
                                                 List<List<? extends FileStorage>> fileStorageLists,
                                                 List<FileStorageAspect> aspectList,
                                                 List<FileWrapperAdapter> fileWrapperAdapterList,
                                                 ContentTypeDetect contentTypeDetect,
                                                 List<List<FileStorageClientFactory<?>>> clientFactoryList) {

        FileStorageServiceBuilder builder = FileStorageServiceBuilder.create(properties.toFileStorageProperties())
                .setFileRecorder(fileRecorder)
                .setAspectList(aspectList)
                .setContentTypeDetect(contentTypeDetect)
                .setFileWrapperAdapterList(fileWrapperAdapterList)
                .setClientFactoryList(clientFactoryList);

        fileStorageLists.forEach(builder::addFileStorage);

        if (properties.getEnableByteFileWrapper()) builder.addByteFileWrapperAdapter();
        if (properties.getEnableUriFileWrapper()) builder.addUriFileWrapperAdapter();
        if (properties.getEnableInputStreamFileWrapper()) builder.addInputStreamFileWrapperAdapter();
        if (properties.getEnableLocalFileWrapper()) builder.addLocalFileWrapperAdapter();
        if (properties.getEnableHttpServletRequestFileWrapper()) builder.addHttpServletRequestFileWrapperAdapter();
        if (properties.getEnableMultipartFileWrapper())
            builder.addFileWrapperAdapter(new MultipartFileWrapperAdapter());
        return builder.build();
    }

    /**
     * 对 FileStorageService 注入自己的代理对象，不然会导致针对 FileStorageService 的代理方法不生效
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshedEvent() {
        FileStorageService service = applicationContext.getBean(FileStorageService.class);
        service.setSelf(service);
    }

}
