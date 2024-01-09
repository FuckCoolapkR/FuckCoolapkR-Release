package org.fuck.coolapk.hook

import android.os.BaseBundle
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.hookAfterMethod

class ModifyAppMode: BaseHook() {

    override fun init() {
        hasEnable("modifyAppMode") {
            BaseBundle::class.java.hookAfterMethod("getString", String::class.java, String::class.java) { param ->
                if (param.args[0] == "APP_MODE") {
                    when (param.result) {
                        "universal" -> param.result = "community"
                        "community" -> param.result = "universal"
                        else -> param.result = "universal"
                    }
                }
            }
        }
    }
}