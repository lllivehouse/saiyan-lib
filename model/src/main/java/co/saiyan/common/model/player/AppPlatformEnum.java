package co.saiyan.common.model.player;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/4
 * @description AppPlatformEnum
 */
public enum AppPlatformEnum {

    ANDROID(0, "安卓"),
    IOS(1, "苹果"),
    ALL(2, "所有"),
    ;

    private Integer platformId;

    private String desc;

    AppPlatformEnum(Integer platformId, String desc) {
        this.platformId = platformId;
        this.desc = desc;
    }

    public static AppPlatformEnum getByPlatformId(Integer platformId) {
        return Arrays.stream(values()).filter(e -> e.getPlatformId().equals(platformId)).findFirst().orElse(null);
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public String getDesc() {
        return desc;
    }

    public static AppPlatformEnum getByName(String platformName) {
        return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(platformName)).findFirst().orElse(null);
    }
}
