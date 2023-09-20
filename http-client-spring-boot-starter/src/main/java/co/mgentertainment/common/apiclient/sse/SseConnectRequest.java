package co.mgentertainment.common.apiclient.sse;

import co.mgentertainment.common.apiclient.core.SseRequest;
import co.mgentertainment.common.apiclient.http.FormatType;
import co.mgentertainment.common.apiclient.http.MethodType;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author larry
 * @createTime 2023/2/12
 * @description SseConnectRequest
 */
@Getter
@ToString
public class SseConnectRequest extends SseRequest {

    public SseConnectRequest(String module, String action, String parameterName, String parameterValue) {
        super(module, action);
        this.setMethod(MethodType.GET);
        this.setHttpContentType(FormatType.TEXT_EVENT_STREAM_VALUE);
        this.setQueryParam(parameterName, parameterValue);
    }

    private void setQueryParam(String parameterName, String parameterValue) {
        if (Objects.nonNull(parameterValue)) {
            this.putQueryParameter(parameterName, parameterValue);
        }
    }
}
