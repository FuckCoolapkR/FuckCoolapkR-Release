package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.callMethod
import org.fuck.coolapk.utils.findClass
import org.fuck.coolapk.utils.hookAfterAllConstructors
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.isNonNull
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log

class RemoveSharePanelItem : BaseHook() {

    private var removeList: MutableList<String> = ArrayList()
    private var removeList2: MutableList<String> = ArrayList()
    override fun init() {
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemFeed", false)) {
            removeList.add("动态")
            removeList2.add("new_feed")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemPM", false)) {
            removeList.add("私信")
            removeList2.add("private_message")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemFeishu", false)) {
            removeList.add("飞书")
            removeList2.add("feishu")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemDyh", false)) {
            removeList.add("看看号")
            removeList2.add("kankanhao")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemWechat", false)) {
            removeList.add("微信")
            removeList2.add("wexin")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemWeMoments", false)) {
            removeList.add("朋友圈")
            removeList2.add("wexin_moment")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemWeibo", false)) {
            removeList.add("微博")
            removeList2.add("weibo")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemQQ", false)) {
            removeList.add("QQ")
            removeList2.add("qq")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemTIM", false)) {
            removeList.add("TIM")
            removeList2.add("tim")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemZhihu", false)) {
            removeList.add("知乎")
            removeList2.add("zhihu")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemCopyLink", false)) {
            removeList.add("复制链接")
            removeList2.add("copy_link")
        }
        if (OwnSP.ownSP.getBoolean("removeSharePanelItemMore", false)) {
            removeList.add("更多")
            removeList2.add("more")
        }
        if (removeList.isEmpty()) return
        CoolContext.dexKit?.findMethod {
            matcher {
                usingStrings("SharePanelCreateMethod", "朋友圈", "com.tencent.mm")
            }
        }.isNonNull {
            if (it.size != 1) {
                log("SharePanelCreateMethod Not found")
                return@isNonNull
            }
            val desc = it.first()
            log("DexKit SharePanelCreateMethod found: ${desc.className}.${desc.name}")
            val clz = desc.className.findClass(CoolContext.classLoader)
            val buildMethod = clz.methodFinder().filter {
                parameterCount == 3 && parameterTypes[1] == String::class.java && parameterTypes[2] == Int::class.java
            }.firstOrNull() ?: run {
                log("build method not found")
                return@isNonNull
            }
            buildMethod.hookBeforeMethod { param ->
                if (removeList.contains(param.args[1])) {
                    param.result = null
                }
            }
        }.isNull {
            log("SharePanelCreateMethod Not found")
        }
        CoolContext.dexKit?.findMethod {
            matcher {
                usingStrings("SharePanelCreateMethod2", "newBuilder().entityType(ENTITY_TYPE_QQ).build()")
            }
        }.isNonNull {
            if (it.isEmpty()) {
                log("SharePanelCreateMethod2 Not found")
                return@isNonNull
            }
            val holderItemClz = "com.coolapk.market.model.HolderItem".findClass(CoolContext.classLoader)
            val desc = it.first()
            log("DexKit SharePanelCreateMethod2 found: ${desc.className}.${desc.name}")
            val clz = desc.className.findClass(CoolContext.classLoader)
            clz.hookAfterAllConstructors { param ->
                param.thisObject.javaClass.declaredFields.forEach { field ->
                    if (field.type == List::class.java) {
                        field.isAccessible = true
                        val iterator = (field.get(param.thisObject) as ArrayList<*>).iterator()
                        while (iterator.hasNext()) {
                            val obj = iterator.next()
                            if (holderItemClz.isInstance(obj)) {
                                if (removeList2.contains(obj.callMethod("getEntityType"))) {
                                    iterator.remove()
                                }
                            } else break
                        }
                    }
                }
            }
        }.isNull {
            log("SharePanelCreateMethod2 Not found")
        }
    }
}