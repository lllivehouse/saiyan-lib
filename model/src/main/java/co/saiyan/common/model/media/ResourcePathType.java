package co.saiyan.common.model.media;

/**
 * @author larry
 * @createTime 2023/9/19
 * @description VideoType
 */
public enum ResourcePathType {
    FEATURE_FILM("film"),
    TRAILER("trailer"),
    SHORT("short"),
    COVER("cover"),
    ORIGIN("origin"),
    ;

    private final String value;

    ResourcePathType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
