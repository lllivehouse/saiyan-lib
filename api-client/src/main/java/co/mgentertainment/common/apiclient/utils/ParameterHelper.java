package co.mgentertainment.common.apiclient.utils;

import cn.hutool.core.codec.Base64;
import co.mgentertainment.common.utils.GsonFactory;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author larry
 * @createTime 2022/12/8
 * @description ParameterHelper
 */
public class ParameterHelper {

    public static String md5Sum(byte[] buff) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(buff);
            return Base64.encode(messageDigest);
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] getXmlData(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Iterator entries = params.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) entries.next();
            xml.append("<" + entry.getKey() + ">");
            xml.append(entry.getValue());
            xml.append("</" + entry.getKey() + ">");
        }

        return xml.toString().getBytes("UTF-8");
    }

    public static byte[] getJsonData(Map<String, Object> params) {
        String json = GsonFactory.getGson().toJson(params);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] getFormData(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator iterator = params.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
            // 过滤file参数
            if (entry.getValue() instanceof File) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()));
            result.append("=");
            result.append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8.name()));
        }

        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static Map<String, File> getFormFiles(Map<String, Object> params) {
        Map<String, File> fileMap = new HashMap<>(16);
        if (Objects.nonNull(params) && !params.isEmpty()) {
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (entry.getValue() instanceof File) {
                    fileMap.put(entry.getKey(), (File) entry.getValue());
                }
            }
        }
        return fileMap;
    }
}
