package co.mgentertainment.common.apiclient.deserializer;

import co.mgentertainment.common.apiclient.core.ApiResponse;
import co.mgentertainment.common.apiclient.exception.ClientException;

import java.util.Map;

/**
 * @author larry
 * @createTime 2022/12/11
 * @description Deserializable
 */
public interface Deserializable {

    /**
     * content反序列化
     *
     * @param clazz
     * @param content
     * @param <T>
     * @return
     * @throws ClientException
     */
    <T extends ApiResponse> T deserialize(Class<T> clazz, String content) throws ClientException;

    /**
     * content转Map
     *
     * @param content
     * @return
     * @throws ClientException
     */
    Map<String, String> toMap(String content) throws ClientException;
}