package org.fuck.coolapk.config

import android.content.Context
import io.fastkv.FastKV
import org.fuck.coolapk.CoolContext

class TagBlacklistConfig(context: Context, name: String) {

    private val key = "TagBlacklist"

    private val kv: FastKV
    private val theList = mutableSetOf<String>()

    init {
        val path = context.filesDir.absolutePath + "fc"
        kv = FastKV.Builder(path, name)
            .build()
        val data: Set<String> = kv.getStringSet(key) ?: mutableSetOf()
        theList.addAll(data)
    }

    val size: Int
        get() {
            synchronized(theList) {
                return theList.size
            }
        }

    fun remove(topic: String) {
        synchronized(theList) {
            theList.remove(topic)
            kv.putStringSet(key, theList)
        }
    }

    fun contains(topic: String): Boolean {
        synchronized(theList) {
            return theList.contains(topic)
        }
    }

    fun clear() {
        synchronized(theList) {
            theList.clear()
            kv.clear()
        }
    }

    fun getAll(): Set<String> {
        synchronized(theList) {
            return theList
        }
    }

    companion object {
        val default = TagBlacklistConfig(CoolContext.context, "tgbl")
    }

}