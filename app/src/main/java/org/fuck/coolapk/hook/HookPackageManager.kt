package org.fuck.coolapk.hook

import android.content.pm.PackageInfo
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.findClass
import org.fuck.coolapk.utils.hookAfterMethod
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.log
import org.hello.coolapk.BuildConfig

class HookPackageManager: BaseHook() {

    override fun init() {
        val applicationPackageManagerClazz = "android.app.ApplicationPackageManager".findClass(CoolContext.classLoader)
        applicationPackageManagerClazz.hookBeforeMethod("getPackageInfo", String::class.java, Int::class.java) {
            if (it.args[0] == BuildConfig.APPLICATION_ID) {
                log("getPackageInfo rejected")
                it.result = null
            }
        }
        applicationPackageManagerClazz.hookAfterMethod("getInstalledPackages", Int::class.java) {
            val result = (it.result as List<*>).toMutableList()
            with(result.iterator()) {
                while (hasNext()) {
                    if ((next() as PackageInfo).packageName == BuildConfig.APPLICATION_ID) {
                        log("getInstalledPackages rejected")
                        remove()
                    }
                }
            }
            it.result = result
        }
    }
}