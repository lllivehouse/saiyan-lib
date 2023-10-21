package co.mgentertainment.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * @author larry
 * @createTime 2022/11/10
 * @description SecurityHelper
 */
@Slf4j
public class SecurityHelper {

    /**
     * 非对称有效期加密
     *
     * @param plainText   明文
     * @param publicKey   公钥
     * @param expiredDate 过期时间
     * @return
     */
    public static String rsaPeriodEncrypt(String plainText, String publicKey, @Nullable Date expiredDate) {
        RSA rsa = new RSA(null, publicKey);
        plainText += Objects.nonNull(expiredDate) ? ';' + expiredDate.getTime() : StringUtils.EMPTY;
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(plainText, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return HexUtil.encodeHexStr(encrypt);
    }

    /**
     * 非对称有效期解密
     *
     * @param encryptText 密文
     * @param privateKey  私钥
     * @return
     */
    public static String rsaPeriodDecrypt(String encryptText, String privateKey) {
        RSA rsa = new RSA(privateKey, null);
        try {
            byte[] decodeHex = HexUtil.decodeHex(encryptText);
            byte[] decrypt = rsa.decrypt(decodeHex, KeyType.PrivateKey);
            String plainText = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
            if (!StringUtils.contains(plainText, ";")) {
                return plainText;
            }
            String[] arr = StringUtils.split(plainText, ";");
            if (arr == null || arr.length != 2) {
                return null;
            }
            Long expiredTimestamp = Long.valueOf(arr[1]);
            if (System.currentTimeMillis() < expiredTimestamp) {
                return arr[0];
            }
        } catch (Exception e) {
            log.error("error to rsa period decrypt", e);
        }
        return null;
    }

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

    public static String rsaDecrypt(String encryptText, String privateKey) {
        RSA rsa = new RSA(privateKey, null);
        try {
            byte[] decodeHex = HexUtil.decodeHex(encryptText);
            byte[] decrypt = rsa.decrypt(decodeHex, KeyType.PrivateKey);
            return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            log.error("error to rsa decrypt", e);
        }
        return null;
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
        Date now = new Date();
        String plainText = rsaDecrypt(encryptText, privateKey);
        String[] arr = StringUtils.split(plainText, ";");
        if (arr == null || arr.length != 2 || !StringUtils.isNumeric(arr[1])) {
            return null;
        }
        Date timeToVerify = new Date(Long.parseLong(arr[1]));
        Date checkingTime = DateUtils.addSeconds(new Date(), nonce);
        if (timeToVerify.before(checkingTime) && timeToVerify.after(now)) {
            return arr[0];
        }
        log.error("RSA DECRYPTION FAILURE. Text:{}, timeToVerify:[{}] should before [{}] and after [{}]",
                arr[0],
                co.mgentertainment.common.utils.DateUtils.format(timeToVerify),
                co.mgentertainment.common.utils.DateUtils.format(checkingTime),
                co.mgentertainment.common.utils.DateUtils.format(now));
        return null;
    }

    /**
     * 混合加密，先对明文用base64编码，再对编码用aes加密
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
     * 混合解密，先对加密问用aes解密，再将解密文用base64解码
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

    public static String aesEncrypt(@NotNull String plainText, @NotNull String secret) {
        AES aes = getAes(secret);
        return aes.encryptHex(plainText);
    }

    public static String aesDecrypt(@NotNull String encryptText, @NotNull String secret) {
        AES aes = getAes(secret);
        return aes.decryptStr(encryptText, StandardCharsets.UTF_8);
    }

    private static AES getAes(String secret) {
        return SecureUtil.aes(secret.getBytes(StandardCharsets.UTF_8));
    }
}