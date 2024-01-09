package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.*

class HookRecyclerViewHolder: BaseHook() {

    override fun init() {
        try {
            XposedBridge.hookAllConstructors(XposedHelpers.findClass("androidx.recyclerview.widget.RecyclerView\$ViewHolder", CoolContext.classLoader), object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    hasEnable("removeFeedAdsEnhance") { hookFeedAdsEnhance(param) }
                }
            })
        } catch (e: Exception) {
            log(e)
        }
    }

    private fun hookFeedAdsEnhance(param: MethodHookParam) {
        param.thisObject.javaClass.methodFinder()
            .findSuper()
            .filterByName("onAdLoadSuccess")
            .firstOrNull()?.createHook {
                replace {
                    MethodFinder.fromClass(param.thisObject.javaClass)
                        .findSuper()
                        .filterByName("onAdClose")
                        .firstOrNull()
                        ?.invoke(it.thisObject)
                }
            }
    }

}