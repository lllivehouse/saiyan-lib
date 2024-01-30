package co.mgentertainment.common.model.exception;

/**
 * 全局通用的errorCode定义
 *
 * @author larry
 */
public enum ErrorCodeEnum {

    INVALID_REQUEST(400, "请求错误"),
    INVALID_PARAM(400, "参数错误"),
    UNAUTHORIZED(401, "您未授权本操作"),
    INVALID_TOKEN(401, "非法或过期的令牌"),
    FORBIDDEN(403, "您没有本操作权限"),
    NOTFOUND(404, "服务未找到"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    SYSTEM_ERROR(500, "系统异常"),
    METHOD_UNSUPPORTED(501, "不支持操作"),
    GATEWAY_FATAL(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    VERSION_NOT_SUPPORT(505, "HTTP版本不支持"),
    API_VALIDATION_FAILED(10001, "open api校验异常"),
    TOKEN_VALIDATION_FAILED(10002, "token校验失败"),
    NOT_ENOUGH_COIN(5001, "金币不足"),
    USER_NOT_FOUND(5002, "用户不存在"),
    USER_INDICATOR_SPEND_FAILED(5003, "用户业务指标扣减异常"),
    NOT_VIP(5004, "非会员"),
    NO_DOWNLOAD_VIP(5005, "无下载次数会员"),
    NOT_INVALID_COIN_VIDEO(5006, "不是合法的金币视频"),
    ALREADY_PAID(5007, "已购买"),
    INVALID_VIDEO(5100, "视频不存在或已下架"),
    VIDEO_NO_FREE_TIMES(5101, "免费次数已用尽"),
    VIDEO_NO_VIP(5102, "非平台会员"),
    VIDEO_NOT_FANS(5103, "未关注UP主"),
    UNCONSUMED_COLLECTION_VIDEO(5104, "未购买合集视频"),
    UNCONSUMED_COIN_VIDEO(5105, "未购买金币视频"),
    ;

    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCodeEnum getByCode(int code) {
        for (ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()) {
            if (errorCodeEnum.getCode() == code) {
                return errorCodeEnum;
            }
        }
        return ErrorCodeEnum.SYSTEM_ERROR;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}