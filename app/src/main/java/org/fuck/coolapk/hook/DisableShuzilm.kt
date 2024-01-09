package org.fuck.coolapk.hook

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.findClass

class DisableShuzilm : BaseHook() {

    override fun init() {
        val shuzilmClazz = "cn.shuzilm.core.DUHelper".findClass(CoolContext.classLoader)
        XposedBridge.hookAllMethods(shuzilmClazz, "init", XC_MethodReplacement.DO_NOTHING)
        XposedBridge.hookAllMethods(shuzilmClazz, "startService", XC_MethodReplacement.DO_NOTHING)
        XposedBridge.hookAllMethods(shuzilmClazz, "loadLibrary", XC_MethodReplacement.DO_NOTHING)
    }

}