package co.saiyan.common.apiclient.deserializer;

import co.saiyan.common.apiclient.core.ApiResponse;
import co.saiyan.common.apiclient.exception.ClientException;
import co.saiyan.common.utils.GsonFactory;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @createTime 2022/12/11
 * @description JsonDeserializer
 */
public class JsonDeserializer implements Deserializable {

    @Override
    public <T extends ApiResponse> T deserialize(Class<T> clazz, String content) throws ClientException {
        try {
            return GsonFactory.getGson().fromJson(content, clazz);
        } catch (Exception ex) {
            throw this.newDeserializeException(clazz, content, ex);
        }
    }

    @Override
    public Map<String, String> toMap(String content) throws ClientException {
        try {
            return GsonFactory.getGson().fromJson(content, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException ex) {
            throw this.newDeserializeException(content, ex);
        }
    }

    private ClientException newDeserializeException(Class<?> clazz, String content, Exception e) {
        return new ClientException("DeserializeFailed",
                "Deserialize response from json content failed, clazz = " + clazz.getSimpleName() + ", origin response " +
                        "= " + content, e);
    }

    private ClientException newDeserializeException(String content, Exception e) {
        return new ClientException("DeserializeFailed",
                "Deserialize response from json content failed, origin response = " + content, e);
    }
}
