package co.mgentertainment.common.model.player;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/10/4
 * @description AppPlatformEnum
 */
public enum AppPlatformEnum {

    ANDROID(0),
    IOS(1),
    ;

    private Integer platformId;

    AppPlatformEnum(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public static AppPlatformEnum getByPlatformId(Integer platformId) {
        return Arrays.stream(AppPlatformEnum.values()).filter(e -> e.getPlatformId().equals(platformId)).findFirst().orElse(null);
    }
}
