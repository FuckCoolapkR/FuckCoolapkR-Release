package org.fuck.coolapk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import org.fuck.coolapk.config.CustomRuleConfig
import org.fuck.coolapk.config.TopicBlacklistConfig
import org.fuck.coolapk.config.UidBlacklistConfig
import org.fuck.coolapk.utils.ContentEncodeUtils
import org.fuck.coolapk.utils.DexKitResultCache
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.log
import org.fuck.coolapk.utils.tryOrNull
import org.luckypray.dexkit.DexKitBridge
import java.util.*

@SuppressLint("StaticFieldLeak")
object CoolContext {
    lateinit var context: Context
    lateinit var classLoader: ClassLoader
    lateinit var activity: Activity
    lateinit var settingsActivity: Activity

    val uidBlacklist by lazy { UidBlacklistConfig.default }
    val topicBlacklist by lazy { TopicBlacklistConfig.default }
    val tagBlacklist by lazy { TopicBlacklistConfig.default }
    val rulesConfig by lazy { CustomRuleConfig.default }
    val dexKitCache by lazy { DexKitResultCache.default }

    var useMemoryDexFile = false

    private var dexKitInner: DexKitBridge? = null
    val dexKit: DexKitBridge?
        get() {
            if (dexKitInner == null) {
                log("DexKit is null, try to init (useMemoryDexFile=$useMemoryDexFile)")
                val helper = DexKitBridge.create(classLoader, useMemoryDexFile)
                helper?.let {
                    dexKitInner = it
                    return it
                }
                log("Cannot create DexKitBridge!!!")
            } else {
                if (dexKitInner!!.isValid) {
                    return dexKitInner
                } else {
                    log("DexKit is invalid, try to init (useMemoryDexFile=$useMemoryDexFile)")
                    val helper = DexKitBridge.create(classLoader, useMemoryDexFile)
                    helper?.let {
                        dexKitInner = it
                        return it
                    }
                    log("Cannot create DexKitBridge!!!")
                }
            }
            return null
        }

    var version: String = ""
    var versionName: String = ""

    val shouldInsertHeadlineCard by lazy {
        val requiredDate = Date().apply {
            month = Calendar.MARCH
            date = 16
        }
        val calendar = GregorianCalendar().apply {
            firstDayOfWeek = Calendar.MONDAY
            time = requiredDate
        }
        val start = calendar.apply { set(Calendar.DAY_OF_WEEK, firstDayOfWeek) }.time.date
        val end = calendar.apply { set(Calendar.DAY_OF_WEEK, firstDayOfWeek + 6) }.time.date
        val now = Date()
        ((now.month == Calendar.MARCH && now.date in start..end) && OwnSP.ownSP.getBoolean("bannerAuto", true) && OwnSP.ownSP.getBoolean("bannerEnable", true)) ||
                (OwnSP.ownSP.getBoolean("bannerForced", false) && OwnSP.ownSP.getBoolean("bannerEnable", false))
    }
    var insertedHeadlineCard = false
    var insertedCustomCard = false

    val feedFilterRegex: List<String> by lazy {
        val result = mutableListOf<String>()
        val list = OwnSP.ownSP.getString("FeedFilterRegex", "")!!.split("|").toMutableList()
        if (!(list.size == 1 && list[0].isEmpty())) {
            list.forEach { tryOrNull { ContentEncodeUtils.decode(it) }?.let { result.add(it) } }
        }
        result
    }
}
