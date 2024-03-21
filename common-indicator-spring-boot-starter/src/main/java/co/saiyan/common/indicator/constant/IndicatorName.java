package co.saiyan.common.indicator.constant;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/7
 * @description IndicatorName
 */
public enum IndicatorName {

    LIKES("likes", "点赞数"),
    COLLECTS("collects", "收藏数"),
    FANS("fans", "粉丝数"),
    FOLLOWS("follows", "关注数"),
    WORKS("works", "作品数"),
    VIEWS("views", "观看数"),
    COMMENTS("comments", "评论数"),
    ;

    private String value;
    private String desc;

    IndicatorName(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static IndicatorName getByValue(String value) {
        return Arrays.stream(values()).filter(e -> e.getValue().equals(value)).findFirst().orElse(null);
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
