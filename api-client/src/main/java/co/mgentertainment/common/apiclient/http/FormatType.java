package co.mgentertainment.common.apiclient.http;

import java.util.Arrays;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description FormatType
 */
public enum FormatType {

    XML(new String[]{"application/xml", "text/xml"}), JSON(new String[]{"application/json", "text/json"}),
    RAW(new String[]{"application/octet-stream"}), FORM(new String[]{"application/x-www-form-urlencoded"}),
    TEXT_EVENT_STREAM_VALUE(new String[]{"text/event-stream"});

    private String[] formats;

    FormatType(String... formats) {
        this.formats = formats;
    }

    public static String mapFormatToAccept(FormatType format) {
        return format.formats[0];
    }

    public static FormatType mapAcceptToFormat(String accept) {
        return Arrays.stream(values()).filter(formatType -> Arrays.asList(formatType.formats).stream().filter(format -> accept.contains(format)).findFirst().isPresent()).findFirst().orElse(null);
    }
}
