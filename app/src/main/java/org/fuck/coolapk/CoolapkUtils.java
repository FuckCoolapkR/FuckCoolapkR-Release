package org.fuck.coolapk;

import org.fuck.coolapk.utils.Base64Utils;
import org.fuck.coolapk.utils.MD5Utils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import de.robv.android.xposed.XposedBridge;

public class CoolapkUtils {

    public static int a = 12345678;

    public static String getAuthString(String deviceCode) {
        try {
            long timestamp = Instant.now().getEpochSecond();
            String b64Timestamp = Base64Utils.encode(String.valueOf(timestamp), true);
            String md5Timestamp = MD5Utils.encode(String.valueOf(timestamp));
            String md5DeviceCode = MD5Utils.encode(deviceCode);
            String token = String.format("token://com.coolapk.market/dcf01e569c1e3db93a3d0fcf191a622c?%s$%s&com.coolapk.market", md5Timestamp, md5DeviceCode);
            String b64Token = Base64Utils.encode(token, true);
            String md5B64Token = MD5Utils.encode(b64Token);
            String md5Token = MD5Utils.encode(token);
            String bcryptSalt = String.format("%s/%su", b64Timestamp.substring(0, 14), md5Token.substring(0, 6));
            String bcryptSaltWithArgs = "$2y$10$" + bcryptSalt;
            String bcrypt = BCrypt.hashpw(md5B64Token, bcryptSaltWithArgs);
            return "v2" + Base64Utils.encode(bcrypt, true);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void loadSO() {
        try {
            byte[] soName = new byte[] { 68, 101, 120, 72, 101, 108, 112, 101, 114 };
            System.loadLibrary(new String(soName, StandardCharsets.UTF_8));
        } catch (Throwable th) {
            XposedBridge.log(th);
        }
    }

    public static native int getFd();

    public static native String readLink(int fd);

}
