package co.mgentertainment.common.model.media;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/9/14
 * @description UploadSubStatusEnum
 */
public enum UploadSubStatusEnum {

    NONE(0, "无"),
    PRINTING(11, "打水印中"),
    UPLOADING_ORIGIN(12, "水印原片上传中"),
    CUTTING_TRAILER(13, "预告片剪切中"),
    UPLOADING_TRAILER(14, "预告片上传中"),
    CUTTING_SHORT(15, "短视频剪切中"),
    UPLOADING_SHORT(16, "短视频上传中"),
    PRINT_FAILURE(17, "打水印失败"),
    UPLOAD_ORIGIN_FAILURE(18, "水印原片上传失败"),
    CUT_TRAILER_FAILURE(19, "预告片剪切失败"),
    UPLOAD_TRAILER_FAILURE(20, "预告片上传失败"),
    CUT_SHORT_FAILURE(21, "短视频剪切失败"),
    UPLOAD_SHORT_FAILURE(22, "短视频上传失败"),
    END(100, "已结束"),
    ;

    private final Integer value;
    private final String desc;

    UploadSubStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UploadSubStatusEnum getByValue(Integer value) {
        return Arrays.stream(UploadSubStatusEnum.values()).filter(item -> item.value.equals(value)).findFirst().orElse(null);
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
