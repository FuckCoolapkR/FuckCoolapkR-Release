package org.fuck.coolapk.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

object HttpUtils {

    private fun readInputStream(stream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(stream))
        val sb = StringBuilder()
        while (true) {
            val temp: String = reader.readLine() ?: break
            sb.append(temp)
        }
        return sb.toString()
    }

    fun get(url: String): String? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.connect()
            if (connection.responseCode != 200) {
                return null
            }
            val stream = connection.inputStream
            readInputStream(stream)
        } catch (e: Exception) {
            null
        }
    }

}