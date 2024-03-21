package co.saiyan.common.apiclient.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author larry
 * @createTime 2023/2/14
 * @description ServerSentMessage
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerSentMessage<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -835036857632813553L;

    private String messageId;

    private T messageBody;
}
