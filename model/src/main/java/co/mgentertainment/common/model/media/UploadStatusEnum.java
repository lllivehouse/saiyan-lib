package co.mgentertainment.common.model.media;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2023/9/14
 * @description UploadStatusEnum
 */
public enum UploadStatusEnum {

    CONVERTING(0, "转码中"),
    UPLOADING(1, "正片上传中"),
    TRAILER_CUTTING_AND_UPLOADING(2, "预告片剪切上传中"),
    CONVERT_FAILURE(3, "转码失败"),
    UPLOAD_FAILURE(4, "正片上传失败"),
    TRAILER_CUT_FAILURE(5, "预告片剪切失败"),
    TRAILER_UPLOAD_FAILURE(6, "预告片上传失败"),
    COMPLETED(10, "已完成"),
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
