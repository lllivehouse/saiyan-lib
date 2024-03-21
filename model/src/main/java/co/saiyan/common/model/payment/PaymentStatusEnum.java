package co.saiyan.common.model.payment;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/11
 * @description PaymentStatusEnum
 */
public enum PaymentStatusEnum {

    PENDING(0, "待支付"),
    SUCCESS(1, "已支付"),
    CANCELED(2, "已取消"),
    FAILURE(3, "支付失败"),
    TIMEOUT(4, "支付超时"),
    ;

    private Integer code;
    private String desc;

    PaymentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaymentStatusEnum getByCode(Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
