package org.fuck.coolapk.hook

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.callStaticMethod
import org.fuck.coolapk.utils.findClass
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.log

class DisableBugly: BaseHook() {

    override fun init() {
        hasEnable("disableBugly") {
            try {
                val buglyClass = "com.tencent.bugly.crashreport.CrashReport".findClass(CoolContext.classLoader)
                XposedBridge.hookAllMethods(buglyClass, "initCrashReport", XC_MethodReplacement.DO_NOTHING)
                buglyClass.callStaticMethod("enableBugly", false)
            } catch (e: Exception) {
                log(e)
            }
        }
    }

}