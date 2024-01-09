package org.fuck.coolapk.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Utils {

    public static String encode(String str, boolean format) {
        String result = Base64.encodeToString(str.getBytes(StandardCharsets.UTF_8), android.util.Base64.DEFAULT);
        if (format) {
            result = format(result);
        }
        return result;
    }

    public static String decode(String str) {
        return new String(Base64.decode(str, android.util.Base64.DEFAULT));
    }

    public static String format(String str) {
        return str.replaceAll("\\r\\n|\\r|\\n|=", "");
    }

}
