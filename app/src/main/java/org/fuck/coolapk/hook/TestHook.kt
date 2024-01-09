package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.callMethodOrNull
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.log

class TestHook: BaseHook() {

    override fun init() {
        hasEnable("enableCoolapkLogOutput") {
            CoolContext.dexKit?.let { dex ->
                val result = dex.findClass {
                    searchPackages("com.coolapk.market.util")
                    matcher {
                        source("LogUtils.java")
                    }
                }
                result.forEach { clazz ->
                    val method = MethodFinder.fromClass(clazz.getInstance(CoolContext.classLoader)).filter {
                        parameterCount == 3 && parameterTypes[0] == String::class.java &&parameterTypes[2] == Array<Any>::class.java
                    }.firstOrNull()
                    method?.let { logMethod ->
                        log(logMethod)
                        logMethod.hookBeforeMethod {
                            val p1 = it.args[0] as String?
                            val p2 = it.args[1] as String?
                            val p3 = it.args[2] as Array<*>
                            val p3String = ""
                            p3.forEachIndexed { index, any ->
                                p3String.plus(" [$index] ${any?.callMethodOrNull("toString")}")
                            }
                            log("Coolapk Logger: $p1 ; $p2 ; $p3")
                        }
                    }
                }
            }
        }
    }

}