package org.fuck.coolapk.hook

import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.hasEnable

class RemoveShareLinkTrack: BaseHook() {

    override fun init() {
        hasEnable("removeShareLinkParams") {
            val banList = arrayListOf("shareUid", "shareFrom")
            XposedHelpers.findAndHookMethod(Uri.Builder::class.java, "appendQueryParameter", String::class.java, String::class.java, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if ((param.args[0] as String?) in banList) {
                        param.result = param.thisObject
                    }
                }
            })
        }
    }

}