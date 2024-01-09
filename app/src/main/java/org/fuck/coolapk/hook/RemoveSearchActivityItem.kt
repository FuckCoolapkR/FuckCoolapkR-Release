package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.callMethod
import org.fuck.coolapk.utils.findClassOrNull
import org.fuck.coolapk.utils.hookAfterMethod
import org.fuck.coolapk.utils.isNonNull
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log

class RemoveSearchActivityItem: BaseHook() {

    override fun init() {
        "com.coolapk.market.view.cardlist.EntityListFragment".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class EntityListFragment not found", online = true) }
            .isNonNull { EntityListFragmentClass ->
                EntityListFragmentClass.methodFinder()
                    .filter { parameterCount == 2 && parameterTypes[0] == List::class.java && parameterTypes[1] == Boolean::class.javaPrimitiveType }
                    .firstOrNull()
                    .isNull { log("Class EntityListFragment Method modifyDataBeforeHandle", online = true) }
                    .isNonNull { method ->
                        method.hookAfterMethod { param ->
                            val new = mutableListOf<Any?>()
                            loop@ for (item in param.result as List<*>) {
                                if (item == null) continue@loop
                                when (item.callMethod("getEntityTemplate") as String?) {
                                    "hotSearch" -> {
                                        if (OwnSP.ownSP.getBoolean("removeSearchActivityItemHotSearch", false)) {
                                            continue@loop
                                        }
                                        new.add(item)
                                    }
                                    "searchHotListCard" -> {
                                        if (OwnSP.ownSP.getBoolean("removeSearchActivityItemHotSearchListCard", false)) {
                                            continue@loop
                                        }
                                        new.add(item)
                                    }
                                    else -> new.add(item)
                                }
                            }
                            param.result = new
                        }
                    }
            }
    }

}