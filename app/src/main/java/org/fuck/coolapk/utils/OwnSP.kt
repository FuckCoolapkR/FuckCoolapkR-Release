package org.fuck.coolapk.utils

import android.content.Context
import android.content.SharedPreferences
import org.fuck.coolapk.Config.SP_NAME
import org.fuck.coolapk.CoolContext

fun hasEnable(key: String, default: Boolean = false, block: () -> Unit) {
    if (OwnSP.ownSP.getBoolean(key, default)) block()
}

object OwnSP {
    val ownSP: SharedPreferences by lazy { CoolContext.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE) }
    private val ownEditor: SharedPreferences.Editor = ownSP.edit()
    fun set(key: String, any: Any) {
        when (any) {
            is Int -> ownEditor.putInt(key, any)
            is Float -> ownEditor.putFloat(key, any)
            is String -> ownEditor.putString(key, any)
            is Boolean -> ownEditor.putBoolean(key, any)
            is Long -> ownEditor.putLong(key, any)
        }
        ownEditor.apply()
    }

    fun remove(key: String) {
        ownEditor.remove(key)
        ownEditor.apply()
    }



}