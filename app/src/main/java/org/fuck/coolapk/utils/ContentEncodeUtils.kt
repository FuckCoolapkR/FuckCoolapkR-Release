package org.fuck.coolapk.utils

import org.springframework.security.crypto.bcrypt.BCrypt

object ContentEncodeUtils {

    fun encode(string: String): String {
        val contentMD5 = MD5Utils.encode(string)
        val contentBase64 = Base64Utils.encode(string, false)
        val encodedSB = StringBuilder()
        val randomIntSB = StringBuilder()
        for (i in contentBase64) {
            while (true) {
                val randomInt = (1..9).random()
                val encoded = i.code xor randomInt
                if (encoded.toChar() == '|') {
                    continue
                }
                encodedSB.append(encoded.toChar())
                randomIntSB.append(randomInt)
                break
            }
        }
        val randomIntBase64 = Base64Utils.encode(randomIntSB.toString(), false)
        val randomIntMD5 = MD5Utils.encode(randomIntSB.toString())
        val salt = String.format("$2a$10$%s", randomIntMD5).substring(0, 31) + "u"
        val hashed = BCrypt.hashpw(contentMD5, salt)
        return "v1RnVja0Nvb2xhcGtS" + Base64Utils.encode(
            String.format(
                "%s|%s|%s",
                encodedSB.toString(),
                hashed,
                randomIntBase64
            ), false
        ).replace("=", "FcR==")
    }

    fun decode(string: String): String? {
        val format = string.run {
            val code = "v1RnVja0Nvb2xhcGtS"
            val index = indexOf(code)
            substring(index + code.length, length)
        }
        val stringDecode = Base64Utils.decode(format.replace("FcR==", "="))
        val split = stringDecode.split("|")
        if (split.size != 3) { return null }
        val encodedString = split[0]
        val hashed = split[1]
        val encodedKey = split[2]
        val key = Base64Utils.decode(encodedKey)
        if (encodedString.length != key.length) { return null }
        val sb = StringBuilder()
        val encodedStringList = encodedString.map { it.code }
        val keyList = key.map { it.toString().toInt() }
        for (i in encodedStringList.indices) { sb.append((encodedStringList[i] xor keyList[i]).toChar()) }
        val result = Base64Utils.decode(sb.toString())
        if (!BCrypt.checkpw(MD5Utils.encode(result), hashed)) { return null }
        return result
    }

}