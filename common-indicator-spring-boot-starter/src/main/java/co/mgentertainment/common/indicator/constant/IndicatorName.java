package co.mgentertainment.common.indicator.constant;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/7
 * @description IndicatorName
 */
public enum IndicatorName {

    User("user"),
    Video("video"),
    Post("post"),
    Seek("seek"),
    ;

    private String key;

    IndicatorName(String key) {
        this.key = key;
    }

    public static IndicatorName getByKey(String key) {
        return Arrays.stream(values()).filter(e -> e.getKey().equals(key)).findFirst().orElse(null);
    }

    public String getKey() {
        return key;
    }
}
