package co.mgentertainment.common.apiclient.exception;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description ClientException
 */
public class ClientException extends RuntimeException {
    private String requestId;
    private String errCode;
    private String errMsg;

    public ClientException(String errCode, String errMsg, String requestId) {
        this(errCode, errMsg);
        this.requestId = requestId;
    }

    public ClientException(String errCode, String errMsg, Throwable cause) {
        super(errCode + " : " + errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ClientException(String errCode, String errMsg) {
        super(errCode + " : " + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + (null == this.getRequestId() ? "" : "\r\nRequestId : " + this.getRequestId()) + (null == this.getErrMsg() ? "" : "\r\nErrorMessage : " + this.getErrMsg());
    }
}