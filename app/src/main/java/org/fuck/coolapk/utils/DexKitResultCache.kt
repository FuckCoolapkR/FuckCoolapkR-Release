package org.fuck.coolapk.utils

import android.content.Context
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import io.fastkv.FastKV
import io.fastkv.interfaces.FastEncoder
import io.packable.PackDecoder
import io.packable.PackEncoder
import org.fuck.coolapk.CoolContext
import java.lang.reflect.Field
import java.lang.reflect.Method

fun DexKitResultCache.CacheItem.Method.getInstance(classLoader: ClassLoader): Method? = tryOrNull {
    val clazz = declaringClassName.findClass(classLoader)
    clazz.methodFinder()
        .filterByName(name)
        .filterByParamCount(parameterTypes.size)
        .filterByParamTypes {
            parameterTypes.forEachIndexed { index, type ->
                if (type != it[index].name) return@filterByParamTypes false
            }
            return@filterByParamTypes true
        }.firstOrNull()
}

class DexKitResultCache(val context: Context, private val versionCode: String) {

    private val kv: FastKV
    private val encoder: FastEncoder<CacheItem>

    init {
        val path = context.filesDir.absolutePath + "/fc"
        encoder = CacheEncoder()
        kv = FastKV.Builder(path, "DexKitCache")
            .encoder(arrayOf(encoder))
            .build()
    }

    fun save(key: String, method: Method) {
        val keyWithVersion = "Method-$key"
        val params = mutableListOf<String>()
        method.parameterTypes.forEach { type ->
            params.add(type.name)
        }
        kv.putObject(keyWithVersion, CacheItem.Method(method.declaringClass.name, method.name, params, versionCode), encoder)
    }

    fun save(key: String, field: Field) {
        val keyWithVersion = "Field-$key"
        kv.putObject(keyWithVersion, CacheItem.Field(field.declaringClass.name, field.name, versionCode), encoder)
    }

    fun save(key: String, clazz: Class<*>) {
        val keyWithVersion = "Class-$key"
        kv.putObject(keyWithVersion, CacheItem.Class(clazz.name, versionCode), encoder)
    }

    fun getMethod(key: String): CacheItem.Method? = tryOrNull {
        val cache = kv.getObject<CacheItem.Method>("Method-$key") ?: return@tryOrNull null
        if (cache.appVersion == versionCode) {
            cache
        } else {
            null
        }
    }

    fun getField(key: String): CacheItem.Field? = tryOrNull {
        val cache = kv.getObject<CacheItem.Field>("Field-$key") ?: return@tryOrNull null
        if (cache.appVersion == versionCode) {
            cache
        } else {
            null
        }
    }

    fun getClass(key: String): CacheItem.Class? = tryOrNull {
        val cache = kv.getObject<CacheItem.Class>("Class-$key") ?: return@tryOrNull null
        if (cache.appVersion == versionCode) {
            cache
        } else {
            null
        }
    }

    sealed class CacheItem {
        data class Method(val declaringClassName: String, val name: String, val parameterTypes: List<String>, val appVersion: String): CacheItem()

        data class Field(val declaringClassName: String, val name: String, val appVersion: String): CacheItem()

        data class Class(val className: String, val appVersion: String): CacheItem()
    }

    private class CacheEncoder: FastEncoder<CacheItem> {
        override fun tag(): String = "DexKitCache"

        override fun encode(obj: CacheItem): ByteArray = when (obj) {
            is CacheItem.Method -> {
                PackEncoder().apply {
                    putInt(0, TYPE_METHOD)
                    putString(1, obj.declaringClassName)
                    putString(2, obj.name)
                    putStringList(3, obj.parameterTypes)
                    putString(4, obj.appVersion)
                }.bytes
            }
            is CacheItem.Field -> {
                PackEncoder().apply {
                    putInt(0, TYPE_FIELD)
                    putString(1, obj.declaringClassName)
                    putString(2, obj.name)
                    putString(3, obj.appVersion)
                }.bytes
            }
            is CacheItem.Class -> {
                PackEncoder().apply {
                    putInt(0, TYPE_CLASS)
                    putString(1, obj.className)
                    putString(2, obj.appVersion)
                }.bytes
            }
        }

        override fun decode(bytes: ByteArray, offset: Int, length: Int): CacheItem {
            val decoder = PackDecoder.newInstance(bytes, offset, length)
            return when (val type = decoder.getInt(0)) {
                TYPE_METHOD -> {
                    val declaringClassName = decoder.getString(1)
                    val name = decoder.getString(2)
                    val parameterTypes = decoder.getStringList(3)
                    val appVersion = decoder.getString(4)
                    CacheItem.Method(declaringClassName, name, parameterTypes, appVersion)
                }
                TYPE_FIELD -> {
                    val declaringClassName = decoder.getString(1)
                    val name = decoder.getString(2)
                    val appVersion = decoder.getString(3)
                    CacheItem.Field(declaringClassName, name, appVersion)
                }
                TYPE_CLASS -> {
                    val className = decoder.getString(1)
                    val appVersion = decoder.getString(2)
                    CacheItem.Class(className, appVersion)
                }
                else -> throw IllegalArgumentException("Unknown type: $type")
            }
        }

    }

    companion object {
        const val TYPE_METHOD = 1
        const val TYPE_FIELD = 2
        const val TYPE_CLASS = 3

        val default by lazy { DexKitResultCache(CoolContext.context, "${CoolContext.versionName}-${CoolContext.version}") }
    }

}