package co.mgentertainment.common.apiclient.auth;

import co.mgentertainment.common.apiclient.exception.ClientException;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description Signer
 */
public abstract class Signer {

    /**
     * 签名
     *
     * @param credential
     * @return
     */
    public abstract String sign(Credential credential);

    /**
     * 签名
     *
     * @param credential
     * @param strToSign
     * @return
     * @throws ClientException
     */
    public abstract String sign(String strToSign, Credential credential) throws ClientException;

    /**
     * 刷新签名
     *
     * @param oldToken
     * @param credential
     * @return
     * @throws ClientException
     */
    public abstract String resign(String oldToken, Credential credential) throws ClientException;
}