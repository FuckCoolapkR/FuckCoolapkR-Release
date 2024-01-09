package org.fuck.coolapk.hook

import android.graphics.Color
import android.widget.Toast
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import de.robv.android.xposed.XC_MethodHook.Unhook
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.callMethod
import org.fuck.coolapk.utils.findClass
import org.fuck.coolapk.utils.getDialogBuilder
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.isNonNull
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log
import org.fuck.coolapk.utils.setScrollView
import org.fuck.coolapk.view.ViewBuilder

class LocalBlackList: BaseHook() {

    private val stringUidBlacklistBlock = "用户屏蔽"
    private val stringUidBlacklistUnblock = "用户解除"
    private val stringTopicBlacklistBlock = "话题屏蔽"
    private val stringTopicBlacklistUnblock = "话题解除"
    private val stringTagBlacklistBlock = "标签屏蔽"
    private val stringTagBlacklistUnblock = "标签解除"
    private val actionUidBlacklistName = "LocalBlackList"
    private val actionUidBlacklistBlock = 233
    private val actionUidBlacklistUnblock = 332
    private val actionTopicBlacklistName = "LocalTopicBlackList"
    private val actionTopicBlacklistBlock = 2333
    private val actionTopicBlacklistUnblock = 3332
    private val actionTagBlacklistName = "LocalTagBlackList"
    private val actionTagBlacklistBlock = 23333
    private val actionTagBlacklistUnblock = 33332

    override fun init() {
        CoolContext.dexKit?.findMethod {
            matcher {
                usingStrings("FeedBuildDialogMethod", "收藏", "举报", "编辑")
            }
        }.isNonNull {
            if (it.size != 1) {
                log("DexKit FeedBuildDialogMethod not found (size=${it.size})")
                return@isNonNull
            }
            val desc = it.first()
            log("DexKit FeedBuildDialogMethod found: ${desc.className}.${desc.name}")
            val clz = desc.className.findClass(CoolContext.classLoader)
            val buildMethod = clz.methodFinder().filter {
                parameterCount != 0 && parameterTypes[0] == clz
            }.firstOrNull() ?: run {
                log("build method not found")
                return@isNonNull
            }
            val actionMethod = clz.methodFinder().filter {
                parameterCount == 1 && returnType != Void.TYPE
            }.firstOrNull() ?: run {
                log("action method not found")
                return@isNonNull
            }
            desc.getMethodInstance(CoolContext.classLoader).hookBeforeMethod {
                var unhook: Unhook? = null
                unhook = buildMethod.hookBeforeMethod r@{ param ->
                    runCatching {
                        val obj = param.args[0]
                        val arg1Clz = param.args[1].javaClass
                        val cons = arg1Clz.declaredConstructors.first()
                        cons.isAccessible = true
                        val field = param.method.declaringClass.declaredFields.firstOrNull { it.type.name == "com.coolapk.market.model.Feed" } ?: run {
                            log("Feed field not found")
                            unhook?.unhook()
                            return@r
                        }
                        field.isAccessible = true
                        val feed = field.get(obj)
                        var isUserInfoNull = false
                        val userInfo = feed?.callMethod("getUserInfo") ?: run {
                            log("userInfo not found")
                            isUserInfoNull = true
                            null
                        }
                        val topicList = arrayListOf<String>()
                        val tTitle = feed?.callMethod("getAppName") as? String ?: run {
                            log("tTitle not found")
                            null
                        }
                        val relations = feed?.callMethod("getRelationRows") as? List<*> ?: run {
                            log("relations not found")
                            null
                        }
                        if (!tTitle.isNullOrEmpty()) {
                            topicList.add(tTitle)
                        }
                        relations?.forEach { item ->
                            val type = item?.callMethod("getTitle") as? String ?: run {
                                log("relation title not found")
                                return@forEach
                            }
                            topicList.add(type)
                        }
                        val tagList = arrayListOf<String>()
                        val tags = feed?.callMethod("getTags") as? String ?: run {
                            log("tags not found")
                            ""
                        }
                        tags.split(",").forEach { tag ->
                            if (tag.isNotEmpty()) {
                                tagList.add(tag)
                            }
                        }
                        if (isUserInfoNull && topicList.isEmpty() && tagList.isEmpty()) {
                            unhook?.unhook()
                            return@r
                        }
                        var tTitleBlacklistBlockArgs : Array<Any>? = null
                        var tTitleBlacklistUnblockArgs : Array<Any>? = null
                        if (topicList.any { CoolContext.topicBlacklist.getAll().contains(it) }) {
                            val unblockArgs = arrayOf(
                                obj,
                                cons.newInstance(actionTopicBlacklistName, actionTopicBlacklistUnblock),
                                stringTopicBlacklistUnblock,
                                android.R.drawable.stat_notify_error,
                                0)
                            val unblock = param.args.copyOf()
                            System.arraycopy(unblockArgs, 0, unblock, 0, unblockArgs.size)
                            tTitleBlacklistUnblockArgs = unblock
                        }
                        if (topicList.any { !CoolContext.topicBlacklist.getAll().contains(it) }) {
                            val blockArgs = arrayOf(
                                obj,
                                cons.newInstance(actionTopicBlacklistName, actionTopicBlacklistBlock),
                                stringTopicBlacklistBlock,
                                android.R.drawable.stat_notify_error,
                                0)
                            val block = param.args.copyOf()
                            System.arraycopy(blockArgs, 0, block, 0, blockArgs.size)
                            tTitleBlacklistBlockArgs = block
                        }
                        var uidBlacklistArgs : Array<Any>? = null
                        userInfo?.let { info ->
                            val uid = (userInfo.callMethod("getUid") as String).toLong()
                            val isBlack = CoolContext.uidBlacklist.contains(uid)
                            val arg1 = cons.newInstance(actionUidBlacklistName, if (isBlack) actionUidBlacklistUnblock else actionUidBlacklistBlock)
                            val arg2 = if (isBlack) stringUidBlacklistUnblock else stringUidBlacklistBlock
                            val arg3 = android.R.drawable.stat_notify_error
                            val arg4 = 0
                            val myArgs = arrayOf(obj, arg1, arg2, arg3, arg4)
                            val origin = param.args.copyOf()
                            System.arraycopy(myArgs, 0, origin, 0, myArgs.size)
                            uidBlacklistArgs = origin
                        }
                        var tagBlacklistBlockArgs : Array<Any>? = null
                        var tagBlacklistUnblockArgs : Array<Any>? = null
                        if (tagList.any { CoolContext.tagBlacklist.getAll().contains(it) }) {
                            val unblockArgs = arrayOf(
                                obj,
                                cons.newInstance(actionTagBlacklistName, actionTagBlacklistUnblock),
                                stringTagBlacklistUnblock,
                                android.R.drawable.stat_notify_error,
                                0)
                            val unblock = param.args.copyOf()
                            System.arraycopy(unblockArgs, 0, unblock, 0, unblockArgs.size)
                            tagBlacklistBlockArgs = unblock
                        }
                        if (tagList.any { !CoolContext.tagBlacklist.getAll().contains(it) }) {
                            val blockArgs = arrayOf(
                                obj,
                                cons.newInstance(actionTagBlacklistName, actionTagBlacklistBlock),
                                stringTagBlacklistBlock,
                                android.R.drawable.stat_notify_error,
                                0)
                            val block = param.args.copyOf()
                            System.arraycopy(blockArgs, 0, block, 0, blockArgs.size)
                            tagBlacklistUnblockArgs = block
                        }
                        unhook?.unhook()
                        uidBlacklistArgs?.let { args ->
                            log("build uid blacklist view")
                            buildMethod.invoke(null, *args)
                        }
                        tTitleBlacklistBlockArgs?.let { args ->
                            log("build topic blacklist block view")
                            buildMethod.invoke(null, *args)
                        }
                        tTitleBlacklistUnblockArgs?.let { args ->
                            log("build topic blacklist unblock view")
                            buildMethod.invoke(null, *args)
                        }
                        tagBlacklistBlockArgs?.let { args ->
                            log("build tag blacklist block view")
                            buildMethod.invoke(null, *args)
                        }
                        tagBlacklistUnblockArgs?.let { args ->
                            log("build tag blacklist unblock view")
                            buildMethod.invoke(null, *args)
                        }
                    }.onFailure {
                        log(it)
                    }
                }
            }
            actionMethod.hookBeforeMethod { param ->
                if ((param.args[0] as Enum<*>).name == actionUidBlacklistName) {
                    log("LocalBlackList action")
                    param.result = null
                    val action = (param.args[0] as Enum<*>).ordinal
                    runCatching {
                        val obj = param.thisObject
                        val field = param.method.declaringClass.declaredFields.firstOrNull { it.type.name == "com.coolapk.market.model.Feed" } ?: run {
                            log("Feed field not found")
                            return@hookBeforeMethod
                        }
                        field.isAccessible = true
                        val feed = field.get(obj)
                        val userInfo = feed?.callMethod("getUserInfo") ?: run {
                            log("userInfo not found")
                            return@hookBeforeMethod
                        }
                        val uid = (userInfo.callMethod("getUid") as String).toLong()
                        val username = userInfo.callMethod("getUserName") as String
                        when (action) {
                            actionUidBlacklistBlock -> {
                                getDialogBuilder(CoolContext.activity).apply {
                                    setTitle("添加到本地黑名单")
                                    setMessage("确定要添加用户: $username uid: $uid 到本地黑名单吗？")
                                    setPositiveButton("确定") { _, _ ->
                                        log("Adding uid: $uid, username: $username to blacklist")
                                        CoolContext.uidBlacklist.add(uid)
                                        Toast.makeText(CoolContext.context, "已添加到本地黑名单", Toast.LENGTH_LONG).show()
                                    }
                                    setNegativeButton("取消", null)
                                    show()
                                }
                            }
                            actionUidBlacklistUnblock -> {
                                getDialogBuilder(CoolContext.activity).apply {
                                    setTitle("从本地黑名单移除")
                                    setMessage("确定要从本地黑名单中移除用户: $username uid: $uid 吗？")
                                    setPositiveButton("确定") { _, _ ->
                                        log("Remove uid: $uid, username: $username from blacklist")
                                        CoolContext.uidBlacklist.remove(uid)
                                        Toast.makeText(CoolContext.context, "已从本地黑名单中移除", Toast.LENGTH_LONG).show()
                                    }
                                    setNegativeButton("取消", null)
                                    show()
                                }
                            }
                            else -> { log("Unknown action code") }
                        }
                    }.onFailure {
                        log(it)
                    }
                }
                if ((param.args[0] as Enum<*>).name == actionTopicBlacklistName) {
                    log("LocalTopicBlackList action")
                    param.result = null
                    val action = (param.args[0] as Enum<*>).ordinal
                    runCatching {
                        val obj = param.thisObject
                        val field = param.method.declaringClass.declaredFields.firstOrNull { it.type.name == "com.coolapk.market.model.Feed" } ?: run {
                            log("Feed field not found")
                            return@hookBeforeMethod
                        }
                        field.isAccessible = true
                        val feed = field.get(obj)
                        val topicList = arrayListOf<String>()
                        val tTitle = feed?.callMethod("getAppName") as? String ?: run {
                            log("tTitle not found")
                            null
                        }
                        val relations = feed?.callMethod("getRelationRows") as? List<*> ?: run {
                            log("relations not found")
                            null
                        }
                        if (!tTitle.isNullOrEmpty()) {
                            topicList.add(tTitle)
                        }
                        relations?.forEach { item ->
                            val type = item?.callMethod("getTitle") as? String ?: run {
                                log("relation title not found")
                                return@forEach
                            }
                            topicList.add(type)
                        }
                        when (action) {
                            actionTopicBlacklistBlock -> {
                                val builder = getDialogBuilder(CoolContext.activity)
                                builder.setScrollView(CoolContext.activity) {
                                    ViewBuilder(CoolContext.activity).apply {
                                        addView(text("选择要添加到本地话题黑名单的话题", 20f))
                                        topicList.forEach { topic ->
                                            if (!CoolContext.topicBlacklist.contains(topic)) {
                                                addView(text(topic, textColor = Color.GREEN) {
                                                    getDialogBuilder(CoolContext.activity).apply {
                                                        setTitle("添加到本地话题黑名单")
                                                        setMessage("确定要添加话题: '$topic' 到本地话题黑名单吗？")
                                                        setPositiveButton("确定") { _, _ ->
                                                            log("Adding topic: $topic to blacklist")
                                                            CoolContext.topicBlacklist.add(topic)
                                                            Toast.makeText(CoolContext.context, "已添加'$topic'到本地话题黑名单", Toast.LENGTH_LONG).show()
                                                        }
                                                        setNegativeButton("取消", null)
                                                        show()
                                                    }
                                                })
                                            }
                                        }
                                    }
                                }.show()
                            }
                            actionTopicBlacklistUnblock -> {
                                val builder = getDialogBuilder(CoolContext.activity)
                                builder.setScrollView(CoolContext.activity) {
                                    ViewBuilder(CoolContext.activity).apply {
                                        addView(text("选择要从本地话题黑名单移除的话题", 20f))
                                        topicList.forEach { topic ->
                                            if (CoolContext.topicBlacklist.contains(topic)) {
                                                addView(text(topic, textColor = Color.GREEN) {
                                                    getDialogBuilder(CoolContext.activity).apply {
                                                        setTitle("从本地话题黑名单移除")
                                                        setMessage("确定要从本地话题黑名单中移除话题: '$topic' 吗？")
                                                        setPositiveButton("确定") { _, _ ->
                                                            log("Remove topic: $topic from blacklist")
                                                            CoolContext.topicBlacklist.remove(topic)
                                                            Toast.makeText(CoolContext.context, "已从本地话题黑名单中移除'$topic'", Toast.LENGTH_LONG).show()
                                                        }
                                                        setNegativeButton("取消", null)
                                                        show()
                                                    }
                                                })
                                            }
                                        }
                                    }
                                }.show()
                            }
                            else -> { log("Unknown action code") }
                        }
                    }.onFailure { log(it) }
                }
                if ((param.args[0] as Enum<*>).name == actionTagBlacklistName) {
                    log("LocalTagBlackList action")
                    param.result = null
                    val action = (param.args[0] as Enum<*>).ordinal
                    runCatching {
                        val obj = param.thisObject
                        val field = param.method.declaringClass.declaredFields.firstOrNull { it.type.name == "com.coolapk.market.model.Feed" } ?: run {
                            log("Feed field not found")
                            return@hookBeforeMethod
                        }
                        field.isAccessible = true
                        val feed = field.get(obj)
                        val tagList = arrayListOf<String>()
                        val tags = feed?.callMethod("getTags") as? String ?: run {
                            log("tags not found")
                            ""
                        }
                        tags.split(",").forEach { tag ->
                            if (tag.isNotEmpty()) {
                                tagList.add(tag)
                            }
                        }
                        when (action) {
                            actionTagBlacklistBlock -> {
                                val builder = getDialogBuilder(CoolContext.activity)
                                builder.setScrollView(CoolContext.activity) {
                                    ViewBuilder(CoolContext.activity).apply {
                                        addView(text("选择要添加到本地标签黑名单的标签", 20f))
                                        tagList.forEach { tag ->
                                            if (!CoolContext.tagBlacklist.contains(tag)) {
                                                addView(text(tag, textColor = Color.GREEN) {
                                                    getDialogBuilder(CoolContext.activity).apply {
                                                        setTitle("添加到本地标签黑名单")
                                                        setMessage("确定要添加标签: '$tag' 到本地标签黑名单吗？")
                                                        setPositiveButton("确定") { _, _ ->
                                                            log("Adding tag: $tag to blacklist")
                                                            CoolContext.tagBlacklist.add(tag)
                                                            Toast.makeText(CoolContext.context, "已添加'$tag'到本地标签黑名单", Toast.LENGTH_LONG).show()
                                                        }
                                                        setNegativeButton("取消", null)
                                                        show()
                                                    }
                                                })
                                            }
                                        }
                                    }
                                }.show()
                            }
                            actionTagBlacklistUnblock -> {
                                val builder = getDialogBuilder(CoolContext.activity)
                                builder.setScrollView(CoolContext.activity) {
                                    ViewBuilder(CoolContext.activity).apply {
                                        addView(text("选择要从本地标签黑名单移除的标签", 20f))
                                        tagList.forEach { tag ->
                                            if (CoolContext.tagBlacklist.contains(tag)) {
                                                addView(text(tag, textColor = Color.GREEN) {
                                                    getDialogBuilder(CoolContext.activity).apply {
                                                        setTitle("从本地标签黑名单移除")
                                                        setMessage("确定要从本地标签黑名单中移除标签: '$tag' 吗？")
                                                        setPositiveButton("确定") { _, _ ->
                                                            log("Remove tag: $tag from blacklist")
                                                            CoolContext.tagBlacklist.remove(tag)
                                                            Toast.makeText(CoolContext.context, "已从本地标签黑名单中移除'$tag'", Toast.LENGTH_LONG).show()
                                                        }
                                                        setNegativeButton("取消", null)
                                                        show()
                                                    }
                                                })
                                            }
                                        }
                                    }
                                }.show()
                            }
                            else -> { log("Unknown action code") }
                        }
                    }
                }
            }
        }.isNull {
            log("Feed Dialog Method Not found")
        }
        CoolContext.dexKit?.findMethod {
            matcher {
                usingStrings("ReplyBuildDialogMethod", "查看会话", "复制", "举报")
            }
        }.isNonNull {
            if (it.size != 1) {
                log("DexKit ReplyBuildDialogMethod not found (size=${it.size})")
                return@isNonNull
            }
            val desc = it.first()
            log("DexKit ReplyBuildDialogMethod found: ${desc.className}.${desc.name}")
            val clz = desc.className.findClass(CoolContext.classLoader)
            val buildMethod = clz.methodFinder().filter {
                parameterCount != 0 && parameterTypes[0] == clz
            }.firstOrNull() ?: run {
                log("build method not found")
                return@isNonNull
            }
            val actionMethod = clz.methodFinder().filter {
                parameterCount == 1 && returnType != Void.TYPE
            }.firstOrNull() ?: run {
                log("action method not found")
                return@isNonNull
            }
            desc.getMethodInstance(CoolContext.classLoader).hookBeforeMethod {
                var unhook: Unhook? = null
                unhook = buildMethod.hookBeforeMethod r@{ param ->
                    runCatching {
                        val obj = param.args[0]
                        val arg1Clz = param.args[1].javaClass
                        val cons = arg1Clz.declaredConstructors.first()
                        cons.isAccessible = true
                        val field = param.method.declaringClass.declaredFields.firstOrNull { it.type.name == "com.coolapk.market.model.FeedReply" } ?: run {
                            log("FeedReply field not found")
                            unhook?.unhook()
                            return@r
                        }
                        field.isAccessible = true
                        val feed = field.get(obj)
                        val userInfo = feed?.callMethod("getUserInfo") ?: run {
                            log("userInfo not found")
                            unhook?.unhook()
                            return@r
                        }
                        val uid = (userInfo.callMethod("getUid") as String).toLong()
                        val isBlack = CoolContext.uidBlacklist.contains(uid)
                        val arg1 = cons.newInstance(actionUidBlacklistName, if (isBlack) actionUidBlacklistUnblock else actionUidBlacklistBlock)
                        val arg2 = if (isBlack) stringUidBlacklistUnblock else stringUidBlacklistBlock
                        val arg3 = android.R.drawable.stat_notify_error
                        val arg4 = 0
                        val myArgs = arrayOf(obj, arg1, arg2, arg3, arg4)
                        val origin = param.args.copyOf()
                        System.arraycopy(myArgs, 0, origin, 0, myArgs.size)
                        unhook?.unhook()
                        buildMethod.invoke(null, *origin)
                    }.onFailure {
                        log(it)
                    }
                }
            }
            actionMethod.hookBeforeMethod { param ->
                if ((param.args[0] as Enum<*>).name == actionUidBlacklistName) {
                    log("LocalBlackList action")
                    param.result = null
                    val action = (param.args[0] as Enum<*>).ordinal
                    runCatching {
                        val obj = param.thisObject
                        val field = param.method.declaringClass.declaredFields.firstOrNull { it.type.name == "com.coolapk.market.model.FeedReply" } ?: run {
                            log("FeedReply field not found")
                            return@hookBeforeMethod
                        }
                        field.isAccessible = true
                        val feed = field.get(obj)
                        val userInfo = feed?.callMethod("getUserInfo") ?: run {
                            log("userInfo not found")
                            return@hookBeforeMethod
                        }
                        val uid = (userInfo.callMethod("getUid") as String).toLong()
                        val username = userInfo.callMethod("getUserName") as String
                        when (action) {
                            actionUidBlacklistBlock -> {
                                log("Adding uid: $uid, username: $username to blacklist")
                                getDialogBuilder(CoolContext.activity).apply {
                                    setTitle("添加到本地黑名单")
                                    setMessage("确定要添加用户: $username uid: $uid 到本地黑名单吗？")
                                    setPositiveButton("确定") { _, _ ->
                                        CoolContext.uidBlacklist.add(uid)
                                        Toast.makeText(CoolContext.context, "已添加到本地黑名单", Toast.LENGTH_LONG).show()
                                    }
                                    setNegativeButton("取消", null)
                                    show()
                                }
                            }
                            actionUidBlacklistUnblock -> {
                                log("Remove uid: $uid, username: $username from blacklist")
                                getDialogBuilder(CoolContext.activity).apply {
                                    setTitle("从本地黑名单移除")
                                    setMessage("确定要从本地黑名单中移除用户: $username uid: $uid 吗？")
                                    setPositiveButton("确定") { _, _ ->
                                        CoolContext.uidBlacklist.remove(uid)
                                        Toast.makeText(CoolContext.context, "已从本地黑名单中移除", Toast.LENGTH_LONG).show()
                                    }
                                    setNegativeButton("取消", null)
                                    show()
                                }
                            }
                            else -> { log("Unknown action code") }
                        }
                    }.onFailure {
                        log(it)
                    }
                }
            }
        }.isNull {
            log("Reply Dialog Method Not found")
        }
    }

}