package co.saiyan.common.model.media;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/9/14
 * @description UploadStatusEnum
 */
public enum UploadStatusEnum {

    CONVERTING(0, "转码中"),
    UPLOADING_FILM(1, "正片上传中"),
    CAPTURING_AND_UPLOADING_COVER(2, "封面剪切上传中"),
    CONVERT_FAILURE(3, "转码失败"),
    UPLOAD_FAILURE(4, "正片上传失败"),
    CAPTURE_FAILURE(5, "封面剪切失败"),
    UPLOAD_COVER_FAILURE(6, "封面上传失败"),
    VIDEO_DAMAGED_OR_LOST(7, "视频已损坏或丢失"),
    COMPLETED(10, "主流程完成"),
    ;

    private final Integer value;
    private final String desc;

    UploadStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UploadStatusEnum getByValue(Integer value) {
        return Arrays.stream(UploadStatusEnum.values()).filter(item -> item.value.equals(value)).findFirst().orElse(null);
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
