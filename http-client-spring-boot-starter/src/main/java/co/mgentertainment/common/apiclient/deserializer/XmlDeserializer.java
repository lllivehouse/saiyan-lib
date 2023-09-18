package co.mgentertainment.common.apiclient.deserializer;

import co.mgentertainment.common.apiclient.core.ApiResponse;
import co.mgentertainment.common.apiclient.exception.ClientException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.util.Map;

/**
 * @author larry
 * @createTime 2022/12/11
 * @description XmlDeserializer
 */
public class XmlDeserializer implements Deserializable {

    @Override
    public <T extends ApiResponse> T deserialize(Class<T> clazz, String content) throws ClientException {
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[]{clazz});
            javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(content));
        } catch (JAXBException ex) {
            throw this.newDeserializeException(clazz, content, ex);
        }
    }

    @Override
    public Map<String, String> toMap(String content) throws ClientException {
        throw new ClientException("operation not support");
    }

    private ClientException newDeserializeException(Class<?> clazz, String xmlContent, Exception e) {
        return new ClientException("DeserializeFailed",
                "Deserialize response from xml content failed, clazz = " + clazz.getSimpleName() + ", origin response =" +
                        " " + xmlContent, e);
    }
}
