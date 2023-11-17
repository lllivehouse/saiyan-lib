package co.mgentertainment.common.model.media;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/9/14
 * @description ResourceTypeEnum
 */
public enum ResourceTypeEnum {

    // 长视频
    VIDEO(0),
    // 短视频
    SHORT(1),
    IMAGE(2),
    PACKAGE(3),
    OTHER(100);

    private final Integer value;

    ResourceTypeEnum(int value) {
        this.value = value;
    }

    public static ResourceTypeEnum getByValue(Integer value) {
        return Arrays.stream(ResourceTypeEnum.values()).filter(item -> item.value.equals(value)).findFirst().orElse(OTHER);
    }

    public int getValue() {
        return value;
    }
}
