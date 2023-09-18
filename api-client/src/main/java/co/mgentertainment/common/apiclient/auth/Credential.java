package co.mgentertainment.common.apiclient.auth;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description Credential
 */
public interface Credential {

    /**
     * 获取加密key
     *
     * @return
     */
    String getEncryptKey();

    /**
     * 获取客户端唯一识别码
     *
     * @return
     */
    String getIdentity();
}
