package co.saiyan.common.uidgen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author larry
 * @createTime 21/08/2023
 * @description SecureProperties
 */
@Data
@ConfigurationProperties(prefix = "uidgen")
public class UidGeneratorProperties {

    /**
     * 初始时间
     */
    private String epochStr = "2023-08-22";

    /**
     * 时间位数，可使用时长为以epochStr为启始，timeBits为增量的时间长度
     */
    private Integer timeBits = 32;

    /**
     * 机器位数，最多支持2^n次机器启动
     */
    private Integer workerBits = 22;

    /**
     * 序列号位数，值越大，每秒支持的并发生成的序列越大
     */
    private Integer seqBits = 9;

    /**
     * RingBuffer size扩容参数 提高UID生成的吞吐量，过高会造成栈溢出
     */
    private Integer boostPower = 3;

}