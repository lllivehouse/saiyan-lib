package co.saiyan.common.apiclient.exception;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description ServerException
 */
public class ServerException extends ClientException {

    public ServerException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ServerException(String errCode, String errMsg, String requestId) {
        this(errCode, errMsg);
        this.setRequestId(requestId);
    }
}