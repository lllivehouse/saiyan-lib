package co.saiyan.common.model.media;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/17
 * @description WatermarkPosition
 */
public enum WatermarkPosition {

    TOP_LEFT(1, "左上"),
    TOP_RIGHT(2, "右上"),
    BOTTOM_LEFT(3, "左下"),
    BOTTOM_RIGHT(4, "右下"),
    ;

    private Integer code;
    private String desc;

    WatermarkPosition(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static WatermarkPosition getByCode(Integer code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }
}
