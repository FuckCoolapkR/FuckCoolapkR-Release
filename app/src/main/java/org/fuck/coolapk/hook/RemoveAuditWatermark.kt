package org.fuck.coolapk.hook

import android.view.View
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.getInstance
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.hookAfterMethod
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log
import java.lang.reflect.Method

class RemoveAuditWatermark: BaseHook() {

    private val textViewClassName = "com.coolapk.market.widget.ForegroundTextView"
    private val feedClassName = "com.coolapk.market.model.Feed"

    override fun init() {
        hasEnable("removeAuditWatermark") {
            findInstallForegroundTextViewMethod()?.let { method ->
                val field = method.declaringClass.declaredFields.firstOrNull { it.type.name == textViewClassName } ?: run {
                    log("Hook installForegroundTextView failed, field not found")
                    return@let
                }
                field.isAccessible = true
                method.hookAfterMethod { param ->
                    val view = field.get(param.thisObject) as? View ?: return@hookAfterMethod
                    view.visibility = View.GONE
                }
            }
            findUpdateFeedMethod()?.let { method ->
                method.hookAfterMethod { param ->
                    val view = param.args[0] as? View ?: return@hookAfterMethod
                    view.visibility = View.GONE
                }
            }
        }
    }

    private fun findInstallForegroundTextViewMethod(): Method? {
        val cache = CoolContext.dexKitCache.getMethod("InstallForegroundTextView")
        cache?.let {
            it.getInstance(CoolContext.classLoader)?.let { method ->
                log("loaded Method installForegroundTextView from cache")
                return method
            }
            log("Method installForegroundTextView from cache cannot get instance")
        }
        val dexKit = CoolContext.dexKit ?: run {
            log("Find installForegroundTextView failed, DexKit is null")
            return null
        }
        val result = dexKit.findMethod {
            matcher {
                usingStrings("动态审核中")
                declaredClass("com.coolapk.market.view.feed.FeedDetailActivityV8")
            }
        }.firstOrNull().isNull { log("Find installForegroundTextView failed, DexKit found nothing") }
        result?.let {
            return it.getMethodInstance(CoolContext.classLoader).also { CoolContext.dexKitCache.save("InstallForegroundTextView", it) }
        }
        return null
    }

    private fun findUpdateFeedMethod(): Method? {
        val cache = CoolContext.dexKitCache.getMethod("UpdateFeed")
        cache?.let {
            it.getInstance(CoolContext.classLoader)?.let { method ->
                log("loaded Method updateFeed from cache")
                return method
            }
            log("Method updateFeed from cache cannot get instance")
        }
        val dexKit = CoolContext.dexKit ?: run {
            log("Find updateFeed failed, DexKit is null")
            return null
        }
        val result = dexKit.findMethod {
            matcher {
                usingStrings("动态审核中")
                paramTypes(textViewClassName, feedClassName)
            }
        }.firstOrNull().isNull { log("Find updateFeed failed, DexKit found nothing") }
        result?.let {
            return it.getMethodInstance(CoolContext.classLoader).also { CoolContext.dexKitCache.save("UpdateFeed", it) }
        }
        return null
    }

}