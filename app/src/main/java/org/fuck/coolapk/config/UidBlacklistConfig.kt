package org.fuck.coolapk.config

import android.content.Context
import io.fastkv.FastKV
import io.fastkv.interfaces.FastEncoder
import io.packable.PackDecoder
import io.packable.PackEncoder
import org.fuck.coolapk.CoolContext

class UidBlacklistConfig(context: Context, name: String) {

    private val key = "blacklist"

    private val kv: FastKV
    private val theList = arrayListOf<Long>()
    init {
        val path = context.filesDir.absolutePath + "/fc"
        kv = FastKV.Builder(path, name)
            .encoder(arrayOf(LongListEncoder.INSTANCE))
            .build()
        val data : List<Long> = kv.getObject(key) ?: arrayListOf()
        theList.addAll(data)
    }

    val size: Int
        get() {
            synchronized(theList) {
                return theList.size
            }
        }

    fun add(id: Long) {
        synchronized(theList) {
            theList.add(id)
            kv.putObject(key, theList, LongListEncoder.INSTANCE)
        }
    }

    fun remove(id: Long) {
        synchronized(theList) {
            theList.remove(id)
            kv.putObject(key, theList, LongListEncoder.INSTANCE)
        }
    }

    fun contains(id: Long): Boolean {
        synchronized(theList) {
            return theList.contains(id)
        }
    }

    fun clear() {
        synchronized(theList) {
            theList.clear()
            kv.clear()
        }
    }

    fun getAll(): List<Long> {
        synchronized(theList) {
            return theList
        }
    }

    companion object {
        val default by lazy { UidBlacklistConfig(CoolContext.context, "bl") }
    }

    private class LongListEncoder : FastEncoder<List<Long>> {

        override fun tag(): String = "LongList"

        override fun encode(obj: List<Long>): ByteArray {
            return PackEncoder().putLongList(0, obj).bytes
        }

        override fun decode(bytes: ByteArray, offset: Int, length: Int): List<Long> {
            return PackDecoder.newInstance(bytes, offset, length).getLongList(0)
        }

        companion object {
            val INSTANCE = LongListEncoder()
        }

    }

}
