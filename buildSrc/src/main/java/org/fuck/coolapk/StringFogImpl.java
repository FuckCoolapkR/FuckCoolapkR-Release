package org.fuck.coolapk;

import com.github.megatronking.stringfog.IStringFog;

import java.nio.charset.StandardCharsets;

public class StringFogImpl implements IStringFog {

    private static int keyXor = 114;

    @Override
    public byte[] encrypt(String data, byte[] key) {
        return xor(data.getBytes(StandardCharsets.UTF_8), key);
    }

    @Override
    public String decrypt(byte[] data, byte[] key) {
        return new String(xor(data, key), StandardCharsets.UTF_8);
    }

    @Override
    public boolean shouldFog(String data) {
        return !data.isEmpty();
    }

    private byte[] xor(byte[] data, byte[] key) {
        int len = data.length;
        int lenKey = key.length;
        int i = 0;
        int j = 0;
        while (i < len) {
            if (j >= lenKey) {
                j = 0;
            }
            data[i] = (byte) (data[i] ^ (key[j] ^ keyXor));
            i++;
            j++;
        }
        return data;
    }

}
