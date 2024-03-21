package co.saiyan.common.apiclient.deserializer;

import co.saiyan.common.apiclient.http.FormatType;

/**
 * @author larry
 * @createTime 2022/12/11
 * @description DeserializableFactory
 */
public class DeserializableFactory {

    public static Deserializable getDeserializer(FormatType format) throws IllegalStateException {
        switch(format) {
            case JSON:
                return new JsonDeserializer();
            default:
                throw new IllegalStateException("Unsupported response format: " + format);
        }
    }
}
