package co.saiyan.common.fileupload;

import cn.hutool.core.collection.CollUtil;
import co.saiyan.common.fileupload.aspect.FileStorageAspect;
import co.saiyan.common.fileupload.exception.FileStorageRuntimeException;
import co.saiyan.common.fileupload.file.*;
import co.saiyan.common.fileupload.platform.*;
import co.saiyan.common.fileupload.recorder.DefaultFileRecorder;
import co.saiyan.common.fileupload.recorder.FileRecorder;
import co.saiyan.common.fileupload.tika.ContentTypeDetect;
import co.saiyan.common.fileupload.tika.DefaultTikaFactory;
import co.saiyan.common.fileupload.tika.TikaContentTypeDetect;
import co.saiyan.common.fileupload.tika.TikaFactory;
import co.saiyan.common.fileupload.util.Tools;
import com.amazonaws.services.s3.AmazonS3;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public class FileStorageServiceBuilder {
    /**
     * 配置参数
     */
    private FileStorageProperties properties;
    /**
     * 文件记录记录者
     */
    private FileRecorder fileRecorder;
    /**
     * Tika 工厂类
     */
    private TikaFactory tikaFactory;
    /**
     * 识别文件的 MIME 类型
     */
    private ContentTypeDetect contentTypeDetect;
    /**
     * 切面
     */
    private List<FileStorageAspect> aspectList = new ArrayList<>();
    /**
     * 文件包装适配器
     */
    private List<FileWrapperAdapter> fileWrapperAdapterList = new ArrayList<>();
    /**
     * Client 工厂
     */
    private List<List<FileStorageClientFactory<?>>> clientFactoryList = new ArrayList<>();
    /**
     * 存储平台
     */
    private List<FileStorage> fileStorageList = new ArrayList<>();


    public FileStorageServiceBuilder(FileStorageProperties properties) {
        this.properties = properties;
    }

    /**
     * 设置默认的文件记录者
     */
    public FileStorageServiceBuilder setDefaultFileRecorder() {
        fileRecorder = new DefaultFileRecorder();
        return this;
    }

    /**
     * 设置默认的 Tika 工厂类
     */
    public FileStorageServiceBuilder setDefaultTikaFactory() {
        tikaFactory = new DefaultTikaFactory();
        return this;
    }

    /**
     * 设置基于 Tika 识别文件的 MIME 类型
     */
    public FileStorageServiceBuilder setTikaContentTypeDetect() {
        if (tikaFactory == null)
            throw new FileStorageRuntimeException("请先设置 TikaFactory");
        contentTypeDetect = new TikaContentTypeDetect(tikaFactory);
        return this;
    }

    /**
     * 添加切面
     */
    public FileStorageServiceBuilder addAspect(FileStorageAspect aspect) {
        aspectList.add(aspect);
        return this;
    }

    /**
     * 添加文件包装适配器
     */
    public FileStorageServiceBuilder addFileWrapperAdapter(FileWrapperAdapter adapter) {
        fileWrapperAdapterList.add(adapter);
        return this;
    }

    /**
     * 添加 byte[] 文件包装适配器
     */
    public FileStorageServiceBuilder addByteFileWrapperAdapter() {
        if (contentTypeDetect == null)
            throw new FileStorageRuntimeException("请先设置 TikaFactory 和 ContentTypeDetect");
        fileWrapperAdapterList.add(new ByteFileWrapperAdapter(contentTypeDetect));
        return this;
    }

    /**
     * 添加 InputStream 文件包装适配器
     */
    public FileStorageServiceBuilder addInputStreamFileWrapperAdapter() {
        if (contentTypeDetect == null)
            throw new FileStorageRuntimeException("请先设置 TikaFactory 和 ContentTypeDetect");
        fileWrapperAdapterList.add(new InputStreamFileWrapperAdapter(contentTypeDetect));
        return this;
    }

    /**
     * 添加本地文件包装适配器
     */
    public FileStorageServiceBuilder addLocalFileWrapperAdapter() {
        if (contentTypeDetect == null)
            throw new FileStorageRuntimeException("请先设置 TikaFactory 和 ContentTypeDetect");
        fileWrapperAdapterList.add(new LocalFileWrapperAdapter(contentTypeDetect));
        return this;
    }

    /**
     * 添加 URI 文件包装适配器
     */
    public FileStorageServiceBuilder addUriFileWrapperAdapter() {
        if (contentTypeDetect == null)
            throw new FileStorageRuntimeException("请先设置 TikaFactory 和 ContentTypeDetect");
        fileWrapperAdapterList.add(new UriFileWrapperAdapter(contentTypeDetect));
        return this;
    }

    /**
     * 添加 HttpServletRequest 文件包装适配器
     */
    public FileStorageServiceBuilder addHttpServletRequestFileWrapperAdapter() {
        if (!doesNotExistClass("javax.servlet.http.HttpServletRequest")) {
            fileWrapperAdapterList.add(new JavaxHttpServletRequestFileWrapperAdapter());
        }
        if (!doesNotExistClass("jakarta.servlet.http.HttpServletRequest")) {
            fileWrapperAdapterList.add(new JakartaHttpServletRequestFileWrapperAdapter());
        }
        return this;
    }

    /**
     * 添加全部的文件包装适配器
     */
    public FileStorageServiceBuilder addAllFileWrapperAdapter() {
        addByteFileWrapperAdapter();
        addInputStreamFileWrapperAdapter();
        addLocalFileWrapperAdapter();
        addUriFileWrapperAdapter();
        addHttpServletRequestFileWrapperAdapter();
        return this;
    }

    /**
     * 添加 Client 工厂
     */
    public FileStorageServiceBuilder addFileStorageClientFactory(List<FileStorageClientFactory<?>> list) {
        clientFactoryList.add(list);
        return this;
    }

    /**
     * 添加 Client 工厂
     */
    public FileStorageServiceBuilder addFileStorageClientFactory(FileStorageClientFactory<?> factory) {
        clientFactoryList.add(Collections.singletonList(factory));
        return this;
    }

    /**
     * 添加存储平台
     */
    public FileStorageServiceBuilder addFileStorage(List<? extends FileStorage> storageList) {
        fileStorageList.addAll(storageList);
        return this;
    }

    /**
     * 添加存储平台
     */
    public FileStorageServiceBuilder addFileStorage(FileStorage storage) {
        fileStorageList.add(storage);
        return this;
    }

    /**
     * 使用默认配置
     */
    public FileStorageServiceBuilder useDefault() {
        setDefaultFileRecorder();
        setDefaultTikaFactory();
        setTikaContentTypeDetect();
        addAllFileWrapperAdapter();
        return this;
    }


    /**
     * 创建
     */
    public FileStorageService build() {
        if (properties == null) throw new FileStorageRuntimeException("properties 不能为 null");

        //初始化各个存储平台
        fileStorageList.addAll(buildLocalFileStorage(properties.getLocal()));
        fileStorageList.addAll(buildLocalPlusFileStorage(properties.getLocalPlus()));
        fileStorageList.addAll(buildAmazonS3FileStorage(properties.getAmazonS3(),clientFactoryList));

        //本体
        FileStorageService service = new FileStorageService();
        service.setSelf(service);
        service.setFileStorageList(new CopyOnWriteArrayList<>(fileStorageList));
        service.setFileRecorder(fileRecorder);
        service.setDefaultPlatform(properties.getDefaultPlatform());
        service.setThumbnailSuffix(properties.getThumbnailSuffix());
        service.setAspectList(new CopyOnWriteArrayList<>(aspectList));
        service.setFileWrapperAdapterList(new CopyOnWriteArrayList<>(fileWrapperAdapterList));
        service.setContentTypeDetect(contentTypeDetect);

        return service;
    }

    /**
     * 创建一个 FileStorageService 的构造器
     */
    public static FileStorageServiceBuilder create(FileStorageProperties properties) {
        return new FileStorageServiceBuilder(properties);
    }

    /**
     * 根据配置文件创建本地文件存储平台
     */
    public static List<LocalFileStorage> buildLocalFileStorage(List<? extends FileStorageProperties.LocalConfig> list) {
        if (CollUtil.isEmpty(list)) return Collections.emptyList();
        return list.stream().map(config -> {
            log.info("加载本地存储平台：{}，此存储平台已不推荐使用，新项目请使用 本地升级版存储平台（LocalPlusFileStorage）",config.getPlatform());
            return new LocalFileStorage(config);
        }).collect(Collectors.toList());
    }

    /**
     * 根据配置文件创建本地文件升级版存储平台
     */
    public static List<LocalPlusFileStorage> buildLocalPlusFileStorage(List<? extends FileStorageProperties.LocalPlusConfig> list) {
        if (CollUtil.isEmpty(list)) return Collections.emptyList();
        return list.stream().map(config -> {
            log.info("加载本地升级版存储平台：{}",config.getPlatform());
            return new LocalPlusFileStorage(config);
        }).collect(Collectors.toList());
    }

    /**
     * 根据配置文件创建又 Amazon S3 存储平台
     */
    public static List<AmazonS3FileStorage> buildAmazonS3FileStorage(List<? extends FileStorageProperties.AmazonS3Config> list, List<List<FileStorageClientFactory<?>>> clientFactoryList) {
        if (CollUtil.isEmpty(list)) return Collections.emptyList();
        buildFileStorageDetect(list," Amazon S3 ","com.amazonaws.services.s3.AmazonS3");
        return list.stream().map(config -> {
            log.info("加载 Amazon S3 存储平台：{}",config.getPlatform());
            FileStorageClientFactory<AmazonS3> clientFactory = getFactory(config.getPlatform(),clientFactoryList,() -> new AmazonS3FileStorageClientFactory(config));
            return new AmazonS3FileStorage(config,clientFactory);
        }).collect(Collectors.toList());
    }

    /**
     * 获取或创建指定存储平台的 Client 工厂对象
     */
    public static <Client> FileStorageClientFactory<Client> getFactory(String platform,List<List<FileStorageClientFactory<?>>> list,Supplier<FileStorageClientFactory<Client>> defaultSupplier) {
        if (list != null) {
            for (List<FileStorageClientFactory<?>> factoryList : list) {
                for (FileStorageClientFactory<?> factory : factoryList) {
                    if (Objects.equals(platform,factory.getPlatform())) {
                        try {
                            return Tools.cast(factory);
                        } catch (Exception e) {
                            throw new FileStorageRuntimeException("获取 FileStorageClientFactory 失败，类型不匹配，platform：" + platform,e);
                        }
                    }
                }
            }
        }
        return defaultSupplier.get();
    }

    /**
     * 判断是否没有引入指定 Class
     */
    public static boolean doesNotExistClass(String name) {
        try {
            Class.forName(name);
            return false;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }

    /**
     * 创建存储平台时的依赖检查
     */
    public static void buildFileStorageDetect(List<?> list,String platformName,String... classNames) {
        if (CollUtil.isEmpty(list)) return;
        for (String className : classNames) {
            if (doesNotExistClass(className)) {
                throw new FileStorageRuntimeException("检测到" + platformName + "配置，但是没有找到对应的依赖类：" + className + "，所以无法加载此存储平台！配置参考地址：https://x-file-storage.xuyanwu.cn/#/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8");
            }
        }
    }

}
