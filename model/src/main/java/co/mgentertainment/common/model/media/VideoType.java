package co.mgentertainment.common.model.media;

/**
 * @author larry
 * @createTime 2023/9/19
 * @description VideoType
 */
public enum VideoType {
    FEATURE_FILM("film"),
    TRAILER("trailer"),
    SHORT_VIDEO("short"),
    ORIGIN_VIDEO("orgin");

    private final String value;

    VideoType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
