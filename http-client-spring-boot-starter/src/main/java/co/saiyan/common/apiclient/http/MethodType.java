package co.saiyan.common.apiclient.http;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description MethodType
 */
public enum MethodType {
    GET("get"),
    PUT("put"),
    POST("post"),
    DELETE("delete"),
    HEAD("head"),
    PATCH("patch"),
    ;

    private String value;

    private MethodType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static MethodType getByValue(String value) {
        return Arrays.stream(MethodType.values()).filter(e -> e.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
    }
}