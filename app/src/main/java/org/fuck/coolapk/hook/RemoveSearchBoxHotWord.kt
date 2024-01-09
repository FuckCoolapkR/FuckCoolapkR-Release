package org.fuck.coolapk.hook

import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.hookBeforeMethod

class RemoveSearchBoxHotWord: BaseHook() {

    override fun init() {
        hasEnable("removeSearchBoxHotWord") {
            "android.app.SharedPreferencesImpl".hookBeforeMethod(CoolContext.classLoader, "getString", String::class.java, String::class.java) { param ->
                if (param.args[0] == "SEARCH_HINT_ARRAY") {
                    param.result = ""
                }
            }
        }
    }

}
