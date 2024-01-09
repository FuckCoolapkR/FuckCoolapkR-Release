package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.callMethod
import org.fuck.coolapk.utils.findClassOrNull
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.isNonNull
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log
import org.fuck.coolapk.utils.tryOrNull

class CheckFeedStatus: BaseHook() {

    override fun init() {
        hasEnable("checkFeedStatus") {
            "com.coolapk.market.model.Feed".findClassOrNull(CoolContext.classLoader)
                .isNull { log("Class Feed not found", online = true) }
                .isNonNull { FeedClass ->
                    FeedClass.methodFinder()
                        .findSuper()
                        .filterByName("getUserName")
                        .firstOrNull()
                        .isNull { log("Class Feed Method getUserName not found", online = true) }
                        .isNonNull { method -> method.createHook {
                            after { param ->
                                tryOrNull { param.thisObject.callMethod("getBlockStatus") as Int? }.isNull { log("getBlockStatus error") }.isNonNull {
                                    if (it != 0) {
                                        param.result = "${param.result} [动态异常](${it})"
                                    }
                                }
                            }
                        } }
                }
        }
    }

}