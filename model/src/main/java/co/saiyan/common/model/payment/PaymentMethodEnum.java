package co.saiyan.common.model.payment;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/11
 * @description PaymentMethodEnum
 */
public enum PaymentMethodEnum {

    ALIPAY(0, "支付宝"),
    WECHAT_PAY(1, "微信支付"),
    UDST(2, "USDT"),
    COIN(3, "金币支付"),
    ;

    private Integer code;
    private String desc;

    PaymentMethodEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaymentMethodEnum getByCode(Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    public static PaymentMethodEnum getByDesc(String desc) {
        return Arrays.stream(values()).filter(e -> e.getDesc().equalsIgnoreCase(desc)).findFirst().orElse(null);
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
