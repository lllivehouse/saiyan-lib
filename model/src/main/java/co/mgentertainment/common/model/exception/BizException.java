package co.mgentertainment.common.model.exception;

/**
 * @author larry
 * @createTime 2022/11/10
 * @description BizException
 */
public class BizException extends RuntimeException {

    private ErrorCodeEnum code;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(ErrorCodeEnum code) {
        super(code.getCode() + "-" + code.getMessage());
        this.code = code;
    }

    public BizException(ErrorCodeEnum code, Throwable cause) {
        super(code.getCode() + "-" + code.getMessage(), cause);
        this.code = code;
    }

    public ErrorCodeEnum getCode() {
        return code;
    }

    public void setCode(ErrorCodeEnum code) {
        this.code = code;
    }
}
