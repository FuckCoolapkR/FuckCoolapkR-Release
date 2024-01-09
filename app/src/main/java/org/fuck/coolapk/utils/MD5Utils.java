package org.fuck.coolapk.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Utils {

    public static String encode(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            return new BigInteger(1, messageDigest.digest(str.getBytes(StandardCharsets.UTF_8))).toString(16);
        } catch (Exception ignored) {}
        throw new RuntimeException("MD5 ERROR");
    }

    public static byte[] encodeToBytes(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            return new BigInteger(messageDigest.digest(str.getBytes(StandardCharsets.UTF_8))).toByteArray();
        } catch (Exception ignored) {}
        throw new RuntimeException("MD5 ERROR");
    }

}
