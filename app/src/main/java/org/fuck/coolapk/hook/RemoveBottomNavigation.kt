package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.CoolapkSP
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.findClassOrNull
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.isNonNull
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log

class RemoveBottomNavigation: BaseHook() {

    private val removeList: List<Boolean> by lazy {
        if (CoolapkSP.isCommunityMode()) {
            listOf(
                OwnSP.ownSP.getBoolean("removeBottomNavigationHome", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationItems", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationDiscovery", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationCenter", false),
            )
        } else {
            listOf(
                OwnSP.ownSP.getBoolean("removeBottomNavigationHome", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationItems", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationDiscovery", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationAppsAndGames", false),
                OwnSP.ownSP.getBoolean("removeBottomNavigationCenter", false),
            )
        }
    }

    override fun init() {
        "com.aurelhubert.ahbottomnavigation.AHBottomNavigation".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class AHBottomNavigation not found", online = true) }
            .isNonNull { AHBottomNavigationClass ->
                AHBottomNavigationClass.methodFinder()
                    .filterByParamCount(1)
                    .filterByParamTypes { it[0] == List::class.java }
                    .firstOrNull()
                    .isNull { log("Class AHBottomNavigation Method null not found", online = true) }
                    .isNonNull { method ->
                        method.hookBeforeMethod { param ->
                            val methodArg = param.args[0] as List<*>
                            val new = mutableListOf<Any?>()
                            methodArg.forEachIndexed { index, any ->
                                if (!removeList[index]) new.add(any)
                            }
                            param.args[0] = new
                        }
                    }
            }
        "com.coolapk.market.view.main.MainFragment".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class MainFragment not found") }
            .isNonNull { MainFragmentClass ->
                MainFragmentClass.methodFinder()
                    .filter { parameterCount == 2 && parameterTypes[0] == Int::class.javaPrimitiveType && parameterTypes[1] == Boolean::class.javaPrimitiveType }
                    .firstOrNull()
                    .isNull { log("Class MainFragment Method onTabSelected not found", online = true) }
                    .isNonNull { method ->
                        method.hookBeforeMethod { param ->
                            val tabInt = param.args[0] as Int
                            var newTabInt = -1
                            for (i in -1 until tabInt) {
                                newTabInt++
                                while (removeList[newTabInt]) {
                                    newTabInt++
                                }
                            }
                            param.args[0] = newTabInt
                        }
                    }
            }
    }

}