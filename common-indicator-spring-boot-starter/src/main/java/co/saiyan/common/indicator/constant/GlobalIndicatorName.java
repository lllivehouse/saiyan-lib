package co.saiyan.common.indicator.constant;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/7
 * @description GlobalIndicatorName
 */
public enum GlobalIndicatorName {

    SEARCH_TIMES("search", "搜索次数"),
    PAGE_VIEWS("pv", "页面访问量"),
    ;

    private String value;
    private String desc;

    GlobalIndicatorName(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static GlobalIndicatorName getByValue(String value) {
        return Arrays.stream(values()).filter(e -> e.getValue().equals(value)).findFirst().orElse(null);
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
