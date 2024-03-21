package co.saiyan.common.uidgen.config;

import co.saiyan.common.uidgen.dal.mapper.WorkerNodeMapper;
import co.saiyan.common.uidgen.impl.CachedUidGenerator;
import co.saiyan.common.uidgen.worker.DisposableWorkNodeService;
import co.saiyan.common.uidgen.worker.WorkNodeService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "co.saiyan.common.uidgen.dal.mapper")
@EnableConfigurationProperties(UidGeneratorProperties.class)
public class UidGenAutoConfiguration {

    @Bean(name = "workNodeService")
    public DisposableWorkNodeService disposableWorkNodeService(WorkerNodeMapper workerNodeMapper) {
        return new DisposableWorkNodeService(workerNodeMapper);
    }

    @Bean(name = "cachedUidGenerator")
    public CachedUidGenerator getUidGenerator(WorkNodeService disposableWorkNodeService, UidGeneratorProperties uidGeneratorProperties) {
        CachedUidGenerator uidGenerator = new CachedUidGenerator();
        uidGenerator.setWorkNodeService(disposableWorkNodeService);
        // 初始时间
        uidGenerator.setEpochStr(uidGeneratorProperties.getEpochStr());
        // 时间位数，可使用时长为以epochStr为启始，timeBits为增量的时间长度
        uidGenerator.setTimeBits(uidGeneratorProperties.getTimeBits());
        // 机器位数，最多支持2^n次机器启动
        uidGenerator.setWorkerBits(uidGeneratorProperties.getWorkerBits());
        // 序列号位数，值越大，每秒支持的并发生成的序列越大
        uidGenerator.setSeqBits(uidGeneratorProperties.getSeqBits());
        // RingBuffer size扩容参数 提高UID生成的吞吐量，过高会造成栈溢出
        uidGenerator.setBoostPower(uidGeneratorProperties.getBoostPower());
        return uidGenerator;
    }
}