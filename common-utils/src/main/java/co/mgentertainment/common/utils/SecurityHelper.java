package co.mgentertainment.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author cl
 * @createTime 2022/11/10
 * @description SecurityHelper
 */
public class SecurityHelper {

    /**
     * 非对称加密
     *
     * @param plainText 明文
     * @param publicKey 公钥
     * @return
     */
    public static String rsaEncrypt(String plainText, String publicKey) {
        RSA rsa = new RSA(null, publicKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(plainText, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return HexUtil.encodeHexStr(encrypt);
    }

    /**
     * 非对称解密
     *
     * @param encryptText 密文
     * @param privateKey  私钥
     * @param nonce       随机数，代表当前时间戳延长的秒数
     * @return
     */
    public static String rsaDecrypt(String encryptText, String privateKey, int nonce) {
        RSA rsa = new RSA(privateKey, null);
        byte[] decodeHex = HexUtil.decodeHex(encryptText);
        byte[] decrypt = rsa.decrypt(decodeHex, KeyType.PrivateKey);
        String decode = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
        String[] arr = StringUtils.split(decode, ";");
        if (arr == null || arr.length != 2 || !StringUtils.isNumeric(arr[1])) {
            return null;
        }
        if (new Date(Long.parseLong(arr[1])).before(DateUtils.addSeconds(new Date(), nonce))) {
            return arr[0];
        }
        return null;
    }

    /**
     * 混合加密，先base64编码，再aes加密，密钥用md5加密
     *
     * @param plainText
     * @param secret
     * @return
     * @throws Exception
     */
    public static String hyperEncrypt(@NotNull String plainText, @NotNull String secret) {
        AES aes = getAes(secret);
        String encodedStr = Base64.encode(plainText);
        return aes.encryptHex(encodedStr);
    }

    /**
     * 混合解密，先aes解密，密钥用md5加密，再base64解码
     *
     * @param encryptText
     * @param secret
     * @return
     * @throws Exception
     */
    public static String hyperDecrypt(@NotNull String encryptText, @NotNull String secret) {
        AES aes = getAes(secret);
        String encodingStr = aes.decryptStr(encryptText, StandardCharsets.UTF_8);
        return Base64.decodeStr(encodingStr);
    }

    private static AES getAes(String secret) {
        String encodingSecret = MD5.create().digestHex(secret);
        return SecureUtil.aes(encodingSecret.getBytes(StandardCharsets.UTF_8));
    }

}