package org.stellar.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public final class UrlUtils {

    private UrlUtils() {}

    public static String getParametersAsString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultStr = result.toString();
        return resultStr.length() > 0 ? resultStr.substring(0, resultStr.length() - 1) : resultStr;
    }

}
