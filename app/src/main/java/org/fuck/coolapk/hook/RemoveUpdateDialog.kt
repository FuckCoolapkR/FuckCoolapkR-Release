package org.fuck.coolapk.hook

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.getInstance
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log
import java.lang.reflect.Method

class RemoveUpdateDialog: BaseHook() {

    override fun init() {
        hasEnable("removeUpdateDialog") {
            findCheckUpdateMethod()?.let {
                XposedBridge.hookMethod(it, XC_MethodReplacement.DO_NOTHING)
            }
        }
    }

    private fun findCheckUpdateMethod(): Method? {
        val cache = CoolContext.dexKitCache.getMethod("CheckUpdate")
        cache?.let {
            it.getInstance(CoolContext.classLoader)?.let { method ->
                log("loaded Method checkUpdate from cache")
                return method
            }
            log("Method checkUpdate from cache cannot get instance")
        }
        val dexKit = CoolContext.dexKit ?: run {
            log("Find CheckUpdateMethod failed, DexKit is null")
            return null
        }
        val result = dexKit.findMethod {
            matcher {
                usingStrings("SHOW_UPGRADE_DIALOG")
                paramTypes()
            }
        }.firstOrNull().isNull { log("Find CheckUpdateMethod failed, DexKit found nothing") }
        result?.let {
            return it.getMethodInstance(CoolContext.classLoader).also { CoolContext.dexKitCache.save("CheckUpdate", it) }
        }
        return null
    }

}