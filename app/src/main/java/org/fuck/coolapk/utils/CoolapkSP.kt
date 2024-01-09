package org.fuck.coolapk.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.XposedEntry

object CoolapkSP {

    val coolapkSP: SharedPreferences by lazy { CoolContext.context.getSharedPreferences("coolapk_preferences_v7", Context.MODE_PRIVATE) }
    private val coolapkEditor: SharedPreferences.Editor = coolapkSP.edit()

    fun set(key: String, any: Any) {
        when (any) {
            is Int -> coolapkEditor.putInt(key, any)
            is Float -> coolapkEditor.putFloat(key, any)
            is String -> coolapkEditor.putString(key, any)
            is Boolean -> coolapkEditor.putBoolean(key, any)
            is Long -> coolapkEditor.putLong(key, any)
        }
        coolapkEditor.apply()
    }

    fun isCommunityMode(): Boolean {
        val coolInfo: PackageInfo = run {
            try {
                CoolContext.context.packageManager.getPackageInfo(CoolContext.context.packageName, PackageManager.GET_META_DATA)
            } catch (e: Exception) {
                log(e)
                null
            }
        } ?: return false
        val metaData = coolInfo.applicationInfo.metaData
        return metaData.getString("APP_MODE", "universal") == "community"
    }

}