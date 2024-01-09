package org.fuck.coolapk

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.system.Os
import android.util.Log
import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.LogExtensions.logexIfThrow
import com.github.kyuubiran.ezxhelper.misc.Observe
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.AbstractCrashesListener
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.crashes.ingestion.models.ErrorAttachmentLog
import com.microsoft.appcenter.crashes.model.ErrorReport
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.config.CustomRuleConfig
import org.fuck.coolapk.hook.CheckFeedStatus
import org.fuck.coolapk.hook.DisableBugly
import org.fuck.coolapk.hook.DisableShuzilm
import org.fuck.coolapk.hook.DisableURLTracking
import org.fuck.coolapk.hook.DisableUmeng
import org.fuck.coolapk.hook.FeedPostChecker
import org.fuck.coolapk.hook.HookPackageManager
import org.fuck.coolapk.hook.HookRecyclerViewHolder
import org.fuck.coolapk.hook.HookSettings
import org.fuck.coolapk.hook.LocalBlackList
import org.fuck.coolapk.hook.ModifyAppMode
import org.fuck.coolapk.hook.ModifyShareUrl
import org.fuck.coolapk.hook.RemoveAds
import org.fuck.coolapk.hook.RemoveAuditWatermark
import org.fuck.coolapk.hook.RemoveBottomNavigation
import org.fuck.coolapk.hook.RemoveSearchActivityItem
import org.fuck.coolapk.hook.RemoveSearchBoxHotWord
import org.fuck.coolapk.hook.RemoveShareLinkTrack
import org.fuck.coolapk.hook.RemoveSharePanelItem
import org.fuck.coolapk.hook.RemoveUpdateDialog
import org.fuck.coolapk.hook.TestHook
import org.fuck.coolapk.utils.ContentEncodeUtils
import org.fuck.coolapk.utils.HttpConfig
import org.fuck.coolapk.utils.HttpUtils
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.forEach
import org.fuck.coolapk.utils.getContext
import org.fuck.coolapk.utils.getStringOrNull
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.log
import org.fuck.coolapk.utils.logXp
import org.fuck.coolapk.utils.onlineLog
import org.fuck.coolapk.utils.tryOrNull
import org.hello.coolapk.BuildConfig
import org.json.JSONObject
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.time.Instant
import kotlin.concurrent.thread

class XposedEntry: IXposedHookLoadPackage {

    private val canCheckVersion = Observe(false) { if (it) checkVersion() }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Config.target) return
        if (lpparam.processName.contains("xg_vip_service")) return
        canCheckVersion.isUnsafeInvoke = true
        val startTime = System.currentTimeMillis()
        EzXHelper.initHandleLoadPackage(lpparam)
        EzXHelper.setLogTag("FuckCoolapkR")
        getContext(lpparam) { context, classLoader ->
            logXp("FuckCoolapkR ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE}) ${BuildConfig.BUILD_TYPE}")
            logXp("Coolapk application time cost: ${System.currentTimeMillis() - startTime}ms")
            canCheckVersion.value = true
            CoolContext.context = context
            CoolContext.classLoader = classLoader
            context.packageManager.getPackageInfo(context.packageName, 0).let {
                CoolContext.version = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) it.longVersionCode.toString() else it.versionCode.toString()
                CoolContext.versionName = it.versionName
            }
            EzXHelper.initAppContext(context)
            EzXHelper.classLoader = classLoader
            init()
            initHooks(
                HookSettings(), // 设置
                RemoveAds(), // 去除广告
                RemoveShareLinkTrack(), // 去除分享动态的 shareUid && shareFrom
                ModifyShareUrl(), // 重定向分享动态链接
                CheckFeedStatus(), // 检测动态状态
                ModifyAppMode(), // 切换酷安模式（正常版/社区版）
                RemoveBottomNavigation(), // 精简首页底部按钮
                RemoveSearchActivityItem(), // 精简搜索界面
                RemoveUpdateDialog(), // 去除酷安更新弹窗
                DisableURLTracking(), // 去除链接追踪
                DisableBugly(),
                DisableUmeng(),
                RemoveSearchBoxHotWord(),
                HookRecyclerViewHolder(), // 增强去广告模式，感觉已经不需要了,
                HookPackageManager(),
                FeedPostChecker(),
                LocalBlackList(),
                RemoveAuditWatermark(), // 去除审核水印
                RemoveSharePanelItem(), // 精简分享面板
                DisableShuzilm(),
                TestHook()
            )
            if (!lpparam.processName.contains("xg_vip_service")) {
                val path = if (context.getExternalFilesDir("rough_draft_backups")?.canWrite() == true) {
                    context.getExternalFilesDir("rough_draft_backups")?.absolutePath
                } else {
                    context.filesDir.absolutePath
                }
                log("rough_draft_backups path: $path")
                listenRoughDraftFile(
                    context.filesDir.resolve("rough_draft").absolutePath,
                    path!!
                )
                log("rough draft listener started")
            }
            log("Module initialized, total time cost: ${System.currentTimeMillis() - startTime}ms")
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        val startTime = System.currentTimeMillis()
        hook.forEach {
            runCatching {
                it.init()
            }.onFailure { log(it) }
        }
        log("initHooks time cost: ${System.currentTimeMillis() - startTime}ms")
    }

    private fun init() {
        // 云控配置
        getOnlineConfig()
        // 获取当前 Activity
        runCatching {
            (CoolContext.context.applicationContext as Application).registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
                override fun onActivityStarted(p0: Activity) {}
                override fun onActivityResumed(p0: Activity) {
                    log("Current activity: ${p0.javaClass}")
                    CoolContext.activity = p0
                }
                override fun onActivityPaused(p0: Activity) {}
                override fun onActivityStopped(p0: Activity) {}
                override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
                override fun onActivityDestroyed(p0: Activity) {}
            })
        }.onFailure {
            log(it)
            Application::class.java.hookBeforeMethod("dispatchActivityResumed", Activity::class.java) {
                val activity = it.args[0]
                log("Current activity: ${activity.javaClass}")
                CoolContext.activity = activity as Activity
            }
        }
        // AppCenter
        runCatching {
            AppCenter.start(CoolContext.context.applicationContext as Application, "ab9866f1-3ec5-4a49-b18d-ddc9266c3cb3", Analytics::class.java, Crashes::class.java)
            Crashes.setListener(object : AbstractCrashesListener() {
                override fun getErrorAttachments(report: ErrorReport): MutableIterable<ErrorAttachmentLog> {
                    val targetPackageInfo = getTargetPackageInfo()
                    val info = if (targetPackageInfo == null) "null" else "Coolapk: ${targetPackageInfo.packageName} - ${targetPackageInfo.versionName}"
                    val textLog = ErrorAttachmentLog.attachmentWithText(
                        "$info\nModule: ${BuildConfig.APPLICATION_ID} - ${BuildConfig.VERSION_NAME}",
                        "debug.txt")
                    return mutableListOf(textLog)
                }
            })
        }
        // 打印所有配置用于debug
        log(OwnSP.ownSP.all)
    }

    private fun getOnlineConfig() {
        thread {
            if ((Instant.now().epochSecond - OwnSP.ownSP.getLong("timestamp", 0L)) > (3600L * 3) || HttpConfig.useTestApi) {
                val api = if (HttpConfig.useTestApi) HttpConfig.testApi else (HttpUtils.get("https://link.fuckcoolapk.workers.dev/") ?: HttpConfig.api)
                val result = HttpUtils.get(api) ?: return@thread
                val response = tryOrNull { JSONObject(result) } ?: return@thread
                tryOrNull { response.getString("redirectLink") }?.let { OwnSP.set("redirectLink", it) }
                tryOrNull { response.getJSONArray("bannerCard").get(0) as JSONObject }?.let {
                    tryOrNull { it.getBoolean("enable") }?.let { OwnSP.set("bannerEnable", it) }
                    tryOrNull { it.getBoolean("auto")}?.let { OwnSP.set("bannerAuto", it) }
                    tryOrNull { it.getBoolean("forced")}?.let { OwnSP.set("bannerForced", it) }
                    tryOrNull { it.getString("url") }?.let { OwnSP.set("bannerUrl", it) }
                    tryOrNull { it.getString("pic") }?.let { OwnSP.set("bannerPic", it) }
                }
                tryOrNull { response.getString("card") }?.let { OwnSP.set("customCard", it) }
                tryOrNull { response.getString("wlimits") }?.let { OwnSP.set("wlimits", it) }
                tryOrNull { response.getJSONArray("rules") }?.let { array ->
                    val rules = arrayListOf<CustomRuleConfig.CustomRule>()
                    array.forEach {
                        val decoded = ContentEncodeUtils.decode(it as? String ?: return@forEach) ?: return@forEach
                        val obj = JSONObject(decoded)
                        val entityType = obj.getStringOrNull("entityType")
                        val title = obj.getStringOrNull("title")
                        val extraData = obj.getStringOrNull("extraData")
                        val url = obj.getStringOrNull("url")
                        val entityTemplate = obj.getStringOrNull("entityTemplate")
                        val entities = obj.getStringOrNull("entities")
                        if (entityType == null && title == null && extraData == null && url == null && entityTemplate == null && entities == null) return@forEach
                        rules.add(CustomRuleConfig.CustomRule(entityType, title, extraData, url, entityTemplate, entities))
                    }
                    if (rules.size > 0) {
                        CoolContext.rulesConfig.replaceAll(rules)
                    }
                }
                OwnSP.set("timestamp", Instant.now().epochSecond)
            }
        }
    }

    private fun listenRoughDraftFile(roughDraftFolder: String, backupFolder: String) {
        thread {
            runCatching {
                File(roughDraftFolder).apply { if (!exists()) mkdirs() }
                val watchService = FileSystems.getDefault().newWatchService()
                val path = Paths.get(roughDraftFolder)
                val draftFile = File(roughDraftFolder, "rough_draft.bin")
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY)
                isDraftListenerStarted = true
                while (true) {
                    val key = watchService.take()
                    for (event in key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            if (event.context().toString() == "rough_draft.bin") {
                                runCatching {
                                    log("rough draft changed")
                                    log("Starting to backup...")
                                    log("Rough Draft File Path: ${draftFile.absolutePath}")
                                    log("Backup Directory Path: $backupFolder")
                                    val backupFileName = "backup_${System.currentTimeMillis()}.bin"
                                    val finalFile = draftFile.copyTo(File(backupFolder, backupFileName), true)
                                    log("backup to ${finalFile.absolutePath}")
                                }.onFailure { log("Rough Draft File Backup Failed: $it") }
                            }
                        }
                    }
                    key.reset()
                }
            }.onFailure { log("Can't start watchService $it") }
        }
    }

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    private fun checkVersion() {
        val versionCode: Int = runCatching {
            val currentUser = Os.geteuid() / 100000
            if (currentUser == -1) return
            val packageName = Config.target
            val activityThreadClazz = Class.forName("android.app.ActivityThread")
            val currentActivityThreadMethod = activityThreadClazz.getDeclaredMethod("currentActivityThread")
            currentActivityThreadMethod.isAccessible = true
            val currentActivityThread = currentActivityThreadMethod.invoke(null)
            val compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo")
            val getPackageInfoMethod = activityThreadClazz.getDeclaredMethod("getPackageInfo", String::class.java, compatibilityInfoClass, Int::class.java, Int::class.java)
            getPackageInfoMethod.isAccessible = true
            val packageInfo = getPackageInfoMethod.invoke(currentActivityThread, packageName, null, 0, currentUser)
            val contextImplClazz = Class.forName("android.app.ContextImpl")
            val loadedApkClazz = Class.forName("android.app.LoadedApk")
            val createAppContextMethod = contextImplClazz.getDeclaredMethod("createAppContext", activityThreadClazz, loadedApkClazz)
            createAppContextMethod.isAccessible = true
            val context = createAppContextMethod.invoke(null, currentActivityThread, packageInfo) as Context? ?: return
            try {
                val fd = CoolapkUtils.getFd()
                if (fd >= 0) {
                    val info = context.packageManager.getPackageArchiveInfo(CoolapkUtils.readLink(fd), PackageManager.GET_META_DATA)
                    logXp("Coolapk Version(fd): ${info?.versionName}(${info?.versionCode})")
                    return@runCatching info?.versionCode
                }
            } catch (ignore: Exception) {}
            val targetPackageInfo = context.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            logXp("Coolapk Version(pn): ${targetPackageInfo.versionName} ${targetPackageInfo.versionCode}")
            targetPackageInfo.versionCode
        }.apply { logexIfThrow{ onlineLog(Log.getStackTraceString(it)) } }.getOrNull() ?: return
        if (versionCode < 2201201) {
            thread { throw RuntimeException("仅支持酷安v12.0.0及以上版本，请升级酷安") }
        }
    }

    fun getTargetPackageInfo(): PackageInfo? {
        return try {
            CoolContext.context.packageManager.getPackageInfo(CoolContext.context.packageName, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            log(e)
            null
        }
    }

    companion object {

        var isDraftListenerStarted = false

        init {
            CoolapkUtils.loadSO()
        }

    }

}
