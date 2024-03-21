package co.saiyan.common.indicator.constant;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/7
 * @description IndicatorCategory
 */
public enum IndicatorCategory {

    USER("user:"),
    VIDEO("video:"),
    POST("post:"),
    SEEK("seek:"),
    COMMENT("comment:"),
    ;

    private String category;

    IndicatorCategory(String category) {
        this.category = category;
    }

    public static IndicatorCategory getByCategory(String category) {
        return Arrays.stream(values()).filter(e -> e.getCategory().equals(category)).findFirst().orElse(null);
    }

    public String getCategory() {
        return category;
    }
}
