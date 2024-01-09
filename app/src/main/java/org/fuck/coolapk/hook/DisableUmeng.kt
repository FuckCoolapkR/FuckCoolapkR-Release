package org.fuck.coolapk.hook

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.findClass
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.log

class DisableUmeng: BaseHook() {

    override fun init() {
        hasEnable("disableUmeng") {
            try {
                val umengClass = "com.umeng.commonsdk.UMConfigure".findClass(CoolContext.classLoader)
                XposedBridge.hookAllMethods(umengClass, "init", XC_MethodReplacement.DO_NOTHING)
                XposedBridge.hookAllMethods(umengClass, "preInit", XC_MethodReplacement.DO_NOTHING)
                XposedBridge.hookAllMethods(umengClass, "getOaid", XC_MethodReplacement.DO_NOTHING)
            } catch (e: Exception) {
                log(e)
            }
        }
    }

}