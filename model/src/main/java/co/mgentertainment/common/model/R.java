package co.mgentertainment.common.model;

import co.mgentertainment.common.model.exception.ErrorCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * 返回对象包装类
 *
 * @param <T>
 * @author larry
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 2271597038398059609L;

    /**
     * 成功标记
     */
    private static final int SUCCESS = 0;
    private static final int OK = 200;

    /**
     * 失败标记
     */
    private static final int FAIL = 1;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg);
    }

    public static <T> R<T> failed(ErrorCodeEnum errorCode) {
        return restResult(null, errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> R<T> failed(ErrorCodeEnum errorCode, String errMsg) {
        return restResult(null, errorCode.getCode(), errMsg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> result(T data, int code, String msg) {
        return restResult(data, code, msg);
    }

    public static <T> R<T> failed(T data, ErrorCodeEnum errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public Boolean isSuccess() {
        return (this.code == SUCCESS || this.code == OK);
    }

}
