package org.fuck.coolapk.hook

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.XposedEntry
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.*
import org.fuck.coolapk.view.ViewBuilder
import org.hello.coolapk.BuildConfig
import java.lang.reflect.Method
import kotlin.system.exitProcess

class HookSettings : BaseHook() {

    private lateinit var dataList: MutableList<Any>
    private var hasHookedOnClick = false
    private var isOpen = false


    override fun init() {
        "com.coolapk.market.view.settings.VXSettingsFragment".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class VXSettingsFragment not found", online = true) }
            .isNonNull { VXSettingsFragmentClass ->
                VXSettingsFragmentClass.methodFinder()
                    .filterByName("initData")
                    .firstOrNull()
                    .isNull { log("Class VXSettingsFragment Method initData not found", online = true) }
                    .isNonNull { method ->
                        method.createHook {
                            before { param ->
                                "com.coolapk.market.model.HolderItem".findClassOrNull(CoolContext.classLoader)
                                    .isNull { log("Class HolderItem not found", online = true) }
                                    .isNonNull { holderItemClass ->
                                        val fuckCoolapkHolderItem = holderItemClass
                                            .callStaticMethod("newBuilder")
                                            ?.callMethod("entityType", "holder_item")
                                            ?.callMethod("string", "Fuck Coolapk R")
                                            ?.callMethod("intValue", 233)
                                            ?.callMethod("build").isNull { log("Settings build item fail") }
                                        val lineHolderItem = holderItemClass
                                            .callStaticMethod("newBuilder")
                                            ?.callMethod("entityType", "holder_item")
                                            ?.callMethod("intValue", 14)
                                            ?.callMethod("build").isNull { log("Settings build item fail") }
                                        VXSettingsFragmentClass.methodFinder()
                                            .findSuper()
                                            .filterByReturnType(List::class.java)
                                            .firstOrNull()
                                            .isNull { log("condition can't find getDataList", online = true) }
                                            ?.invoke(param.thisObject).apply {
                                                dataList = (this as MutableList<Any>).apply {
                                                    fuckCoolapkHolderItem?.let { add(it) }
                                                    lineHolderItem?.let { add(it) }
                                                }
                                            }
                                    }
                                tryOrNull { param.thisObject.callMethod("requireActivity") as Activity? }
                                    .isNull {
                                        log("requireActivity error"); CoolContext.settingsActivity =
                                        CoolContext.activity
                                    }
                                    .isNonNull { CoolContext.settingsActivity = it }
                            }
                        }
                    }
            }
        findSetVXSettingsOnClickMethod()?.hookBeforeMethod { param ->
            val intValue = dataList[param.args[0].callMethod("getAdapterPosition") as Int].callMethod("getIntValue")
            if (intValue == 233 && !isOpen) {
                showSettingsDialog()
            }
        }
    }


    private fun findSetVXSettingsOnClickMethod(): Method? {
        val cache = CoolContext.dexKitCache.getMethod("SetVXSettingsOnClick")
        if (cache != null) {
            val methodInstance = cache.getInstance(CoolContext.classLoader)
            if (methodInstance != null) {
                log("loaded Method SetVXSettingsOnClick from cache")
                return methodInstance
            } else {
                log("Method SetVXSettingsOnClick from cache cannot get instance")
            }
        }
        val dexKit = CoolContext.dexKit ?: run {
            log("Find SetVXSettingsOnClick failed, DexKit is null")
            return null
        }
        val result = dexKit.findMethod {
            matcher {
                paramTypes("androidx.recyclerview.widget.RecyclerView\$ViewHolder", null)
                returnType("void")
            }
            searchPackages("com.coolapk.market.view.settings")
        }.firstOrNull { it.className.startsWith("com.coolapk.market.view.settings.VXSettingsFragment") }
        if (result == null) {
            log("HookSettings, findMethod fail")
        }
        return result?.getMethodInstance(CoolContext.classLoader)
            ?.also { CoolContext.dexKitCache.save("SetVXSettingsOnClick", it) }
    }

    private fun showDebugDialog() {
        with(CoolContext.settingsActivity) {
            val builder = getDialogBuilder(this)
            builder.setScrollView(this) {
                ViewBuilder(this@with).apply {
                    addView(text("Debug", 26f))
                    addView(text("导出 Dex", onClickListener = {
                        val path = this@with.getExternalFilesDir("dexOut-${System.currentTimeMillis()}")?.absolutePath
                        try {
                            CoolContext.dexKit?.exportDexFile(path!!)
                            Toast.makeText(this@with, "导出到路径: $path", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@with, "导出失败", Toast.LENGTH_SHORT).show()
                            log(e)
                        }
                    }))
                    addView(textWithSwitch("输出酷安Log", "enableCoolapkLogOutput"))
                }
            }
            builder.show()
        }
    }

    private fun showSettingsDialog() {
        var nameClickTime = 0
        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            ViewBuilder(CoolContext.settingsActivity).apply {
                addView(text("Fuck Coolapk R", 26f, onClickListener = {
                    nameClickTime += 1
                    if (nameClickTime >= 5) {
                        showDebugDialog()
                    }
                }))
                addView(text("${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}", 26f))
                addView(text("修改功能后重启酷安生效", 12f, Color.parseColor("#ff0000")))
                if (!XposedEntry.isDraftListenerStarted) {
                    addView(text("*草稿箱监听未启动", 12f, Color.parseColor("#ff0000")))
                }
                addView(text("精简", 20f, Color.parseColor("#0caa64")))
                addView(text("去除信息流广告", onClickListener = { showRemoveFeedAdsDialog() }))
                addView(textWithSwitch("去除动态下方广告", "removeBannerAds"))
                addView(textWithSwitch("去除动态下方「提到的好物」", "removeFeedGoods"))
                addView(textWithSwitch("去除搜索框热词", "removeSearchBoxHotWord"))
                addView(textWithSwitch("去除动态审核水印", "removeAuditWatermark"))
                addView(textWithSwitch("禁用Bugly", "disableBugly"))
                addView(textWithSwitch("禁用Umeng", "disableUmeng"))
                addView(text("精简首页底部按钮", onClickListener = { showRemoveBottomNavigationDialog() }))
                addView(text("精简搜索界面", onClickListener = { showRemoveSearchActivityItemDialog() }))
                addView(text("精简分享面板", onClickListener = { showRemoveSharePanelItemDialog() }))
                addView(text("加强", 20f, Color.parseColor("#0caa64")))
                addView(textWithSwitch("检测动态状态", "checkFeedStatus"))
                addView(textWithSwitch("重定向分享动态链接", "modifyShareUrl"))
                addView(text("自定义动态内容黑名单", onClickListener = { showModifyFeedFilterDialog() }))
                addView(text("本地用户黑名单", onClickListener = { showLocalBlacklistDialog() }))
                addView(text("本地话题黑名单", onClickListener = { showLocalTopicBlacklistDialog() }))
                addView(text("本地标签黑名单", onClickListener = { showLocalTagBlacklistDialog() }))
                addView(text("其他", 20f, Color.parseColor("#0caa64")))
                addView(
                    textWithSwitch(
                        "将版本数据修改为14.0.2",
                        "customversiondata",
                        doubleConfirm = true,
                        message = "该功能仅支持Coolapk 13.0.1版本。\n 该功能尚处于实验阶段，若启用，可能导致若干问题。请谨慎使用。"
                    )
                )
                addView(textWithSwitch("去除更新弹窗", "removeUpdateDialog"))
                addView(textWithSwitch("拒绝分享动态链接时附加「shareFrom」「shareUid」", "removeShareLinkParams"))
                addView(textWithSwitch("切换酷安模式（正常版/社区版）", "modifyAppMode"))
                addView(textWithSwitch("输出Debug日志", "debugLog", true))
                addView(text("使用备份文件恢复草稿箱", onClickListener = { showRestoreDraftDialog() }))
            }
        }
        builder.setPositiveButton("重启酷安") { _, _ -> exitProcess(0) }
        builder.setOnDismissListener { isOpen = false }
        builder.setOnCancelListener { isOpen = false }
        builder.show()
        isOpen = true
    }

    private fun showLocalTagBlacklistDialog() {
        var baseDialog: AlertDialog? = null
        fun confirmDialog() {
            val builder = getDialogBuilder(CoolContext.settingsActivity)
            builder.apply {
                setTitle("警告")
                setMessage("确定要清空本地标签黑名单吗？")
                setPositiveButton("确定") { _, _ ->
                    CoolContext.tagBlacklist.clear()
                    Toast.makeText(CoolContext.context, "已清空本地标签黑名单", Toast.LENGTH_LONG).show()
                    baseDialog?.dismiss()
                }
                setNegativeButton("取消", null)
                show()
            }
        }

        fun confirmDialog(value: String, callback: () -> Unit) {
            val builder = getDialogBuilder(CoolContext.settingsActivity)
            builder.apply {
                setTitle("从本地标签黑名单移除")
                setMessage("确定要从本地标签黑名单中移除标签: '$value' 吗？")
                setPositiveButton("确定") { _, _ ->
                    log("Remove tag: $value from blacklist")
                    CoolContext.tagBlacklist.remove(value)
                    Toast.makeText(CoolContext.context, "已从本地标签黑名单中移除'$value'", Toast.LENGTH_LONG).show()
                    callback()
                }
                setNegativeButton("取消", null)
                show()
            }
        }

        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.apply {
            setScrollView(CoolContext.settingsActivity) {
                ViewBuilder(CoolContext.settingsActivity).apply {
                    addView(text("本地标签黑名单", 20f))
                    addView(textWithSwitch("不看黑名单标签的动态", "tagBlacklist_noFeed"))
                    addView(text("清空黑名单", textColor = Color.parseColor("#ff0000")) {
                        confirmDialog()
                    })
                    val rulesLayout = LinearLayout(CoolContext.settingsActivity)
                    rulesLayout.apply {
                        orientation = LinearLayout.VERTICAL
                    }
                    lateinit var listener: (TextView) -> Unit
                    val callback: () -> Unit = {
                        rulesLayout.removeAllViews()
                        for (item in CoolContext.tagBlacklist.getAll()) {
                            rulesLayout.addView(text(item, onClickListener = listener))
                        }
                    }
                    listener = {
                        confirmDialog(it.text.toString(), callback)
                    }
                    for (item in CoolContext.tagBlacklist.getAll()) {
                        rulesLayout.addView(text(item, onClickListener = listener))
                    }
                    addView(rulesLayout)
                }
            }
            baseDialog = show()
        }
    }

    private fun showLocalTopicBlacklistDialog() {
        var baseDialog: AlertDialog? = null
        fun confirmDialog() {
            val builder = getDialogBuilder(CoolContext.settingsActivity)
            builder.apply {
                setTitle("警告")
                setMessage("确定要清空本地话题黑名单吗？")
                setPositiveButton("确定") { _, _ ->
                    CoolContext.topicBlacklist.clear()
                    Toast.makeText(CoolContext.context, "已清空本地话题黑名单", Toast.LENGTH_LONG).show()
                    baseDialog?.dismiss()
                }
                setNegativeButton("取消", null)
                show()
            }
        }

        fun confirmDialog(value: String, callback: () -> Unit) {
            val builder = getDialogBuilder(CoolContext.settingsActivity)
            builder.apply {
                setTitle("从本地话题黑名单移除")
                setMessage("确定要从本地话题黑名单中移除话题: '$value' 吗？")
                setPositiveButton("确定") { _, _ ->
                    log("Remove topic: $value from blacklist")
                    CoolContext.topicBlacklist.remove(value)
                    Toast.makeText(CoolContext.context, "已从本地话题黑名单中移除'$value'", Toast.LENGTH_LONG).show()
                    callback()
                }
                setNegativeButton("取消", null)
                show()
            }
        }

        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.apply {
            setScrollView(CoolContext.settingsActivity) {
                ViewBuilder(CoolContext.settingsActivity).apply {
                    addView(text("本地话题黑名单", 20f))
                    addView(textWithSwitch("不看话题的动态", "topicBlacklist_noFeed"))
                    addView(text("清空黑名单", textColor = Color.parseColor("#ff0000")) {
                        confirmDialog()
                    })
                    val rulesLayout = LinearLayout(CoolContext.settingsActivity)
                    rulesLayout.apply {
                        orientation = LinearLayout.VERTICAL
                    }
                    lateinit var listener: (TextView) -> Unit
                    val callback: () -> Unit = {
                        rulesLayout.removeAllViews()
                        for (item in CoolContext.topicBlacklist.getAll()) {
                            rulesLayout.addView(text(item, onClickListener = listener))
                        }
                    }
                    listener = {
                        confirmDialog(it.text.toString(), callback)
                    }
                    for (item in CoolContext.topicBlacklist.getAll()) {
                        rulesLayout.addView(text(item, onClickListener = listener))
                    }
                    addView(rulesLayout)
                }
            }
            baseDialog = show()
        }
    }

    private fun showLocalBlacklistDialog() {
        fun confirmDialog() {
            val builder = getDialogBuilder(CoolContext.settingsActivity)
            builder.apply {
                setTitle("警告")
                setMessage("确定要清空本地黑名单吗？")
                setPositiveButton("确定") { _, _ ->
                    CoolContext.uidBlacklist.clear()
                    Toast.makeText(CoolContext.context, "已清空本地黑名单", Toast.LENGTH_LONG).show()
                }
                setNegativeButton("取消", null)
                show()
            }
        }

        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            ViewBuilder(CoolContext.settingsActivity).apply {
                addView(text("本地用户黑名单", 20f))
                addView(textWithSwitch("不看TA的动态", "blacklist_noFeed"))
                addView(textWithSwitch("不看TA的回复", "blacklist_noReply", doubleConfirm = true))
                addView(text("清空黑名单", textColor = Color.parseColor("#ff0000"), onClickListener = {
                    confirmDialog()
                }))
            }
        }.show()
    }

    private fun showRestoreDraftDialog() {
        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.apply {
            setTitle("使用备份文件恢复草稿箱")
            setMessage("点击确认后，模块会拉起文件选取器，请在弹出的文件选择器中选择备份文件")
            setPositiveButton("确认") { _, _ ->
                RestoreDraft.start(CoolContext.settingsActivity)
            }
            setNegativeButton("取消") { _, _ -> }
            show()
        }
    }

    private fun showRemoveFeedAdsDialog() {
        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            ViewBuilder(CoolContext.settingsActivity).apply {
                addView(text("去除信息流广告", 20f))
                addView(textWithSwitch("启用", "removeFeedAds"))
                addView(textWithSwitch("启用(增强)", "removeFeedAdsEnhance", doubleConfirm = true))
                addView(textWithSwitch("debug(开发者功能，闲人用不上)", "removeFeedAdsDebug", doubleConfirm = true))
            }
        }.show()
    }

    private fun showRemoveBottomNavigationDialog() {
        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            ViewBuilder(CoolContext.settingsActivity).apply {
                addView(text("精简首页底部按钮", 20f))
                addView(textWithSwitch("首页", "removeBottomNavigationHome"))
                addView(textWithSwitch("数码", "removeBottomNavigationItems"))
                addView(textWithSwitch("发现", "removeBottomNavigationDiscovery"))
                if (!CoolapkSP.isCommunityMode()) addView(
                    textWithSwitch(
                        "应用游戏",
                        "removeBottomNavigationAppsAndGames"
                    )
                )
            }
        }.show()
    }

    private fun showRemoveSearchActivityItemDialog() {
        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            ViewBuilder(CoolContext.settingsActivity).apply {
                addView(text("精简搜索界面", 20f))
                addView(textWithSwitch("热门搜索", "removeSearchActivityItemHotSearch"))
                addView(textWithSwitch("热榜", "removeSearchActivityItemHotSearchListCard"))
            }
        }.show()
    }

    private fun showRemoveSharePanelItemDialog() {
        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            ViewBuilder(CoolContext.settingsActivity).apply {
                addView(text("精简分享面板", 20f))
                addView(textWithSwitch("动态", "removeSharePanelItemFeed"))
                addView(textWithSwitch("私信", "removeSharePanelItemPM"))
                addView(textWithSwitch("飞书", "removeSharePanelItemFeishu"))
                addView(textWithSwitch("看看号", "removeSharePanelItemDyh"))
                addView(textWithSwitch("微信", "removeSharePanelItemWechat"))
                addView(textWithSwitch("朋友圈", "removeSharePanelItemWeMoments"))
                addView(textWithSwitch("微博", "removeSharePanelItemWeibo"))
                addView(textWithSwitch("QQ", "removeSharePanelItemQQ"))
                addView(textWithSwitch("TIM", "removeSharePanelItemTIM"))
                addView(textWithSwitch("知乎", "removeSharePanelItemZhihu"))
                addView(textWithSwitch("复制链接", "removeSharePanelItemCopyLink"))
                addView(textWithSwitch("更多", "removeSharePanelItemMore"))
            }
        }.show()
    }

    private fun showModifyFeedFilterDialog() {
        fun getCurrentFeedFilter(): List<String> {
            val result = mutableListOf<String>()
            val list = OwnSP.ownSP.getString("FeedFilterRegex", "")!!.split("|").toMutableList()
            if (!(list.size == 1 && list[0].isEmpty())) {
                list.forEach { tryOrNull { ContentEncodeUtils.decode(it) }?.let { result.add(it) } }
            }
            return result
        }

        fun setFeedFilter(list: List<String>) {
            val sb = StringBuilder()
            list.forEach {
                sb.append(ContentEncodeUtils.encode(it))
                sb.append("|")
            }
            var result = sb.toString()
            if (result.isNotEmpty() && result.last() == '|') {
                result = result.substring(0, result.length - 1)
            }
            OwnSP.set("FeedFilterRegex", result)
        }

        val builder = getDialogBuilder(CoolContext.settingsActivity)
        builder.setScrollView(CoolContext.settingsActivity) {
            var regex = ""
            lateinit var editText: EditText
            lateinit var listener: ((TextView) -> Unit)
            ViewBuilder(CoolContext.settingsActivity).apply {
                val regexesView = LinearLayout(CoolContext.settingsActivity).also {
                    it.orientation = LinearLayout.VERTICAL
                }
                listener = { textView ->
                    setFeedFilter(getCurrentFeedFilter().toMutableList().also { it.remove(textView.text) })
                    regexesView.removeAllViews()
                    getCurrentFeedFilter().forEach {
                        regexesView.addView(
                            text(
                                it,
                                14f,
                                Color.parseColor("#ff0000"),
                                listener
                            )
                        )
                    }
                }
                getCurrentFeedFilter().forEach {
                    regexesView.addView(
                        text(
                            it,
                            14f,
                            Color.parseColor("#ff0000"),
                            listener
                        )
                    )
                }
                addView(text("自定义动态内容黑名单", 20f))
                addView(text("请输入正则表达式规则：", 18f))
                addView(editText { regex = it }.also { editText = it })
                addView(text("例子：(摄影|美食家) 表示匹配含有摄影或美食家"))
                addView(button("添加") {
                    if (regex.isEmpty()) {
                        Toast.makeText(CoolContext.context, "不可为空", Toast.LENGTH_SHORT).show()
                    } else {
                        setFeedFilter(getCurrentFeedFilter().toMutableList().also { it.add(regex) })
                        runCatching { editText.setText("") }
                        regexesView.removeAllViews()
                        getCurrentFeedFilter().forEach {
                            regexesView.addView(
                                text(
                                    it,
                                    14f,
                                    Color.parseColor("#ff0000"),
                                    listener
                                )
                            )
                        }
                    }
                })
                addView(text("当前已有规则："))
                addView(regexesView)
            }
        }.show()
    }

}
