package org.fuck.coolapk.hook

import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.config.CustomRuleConfig
import org.fuck.coolapk.utils.CustomCardBuilder
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.callMethod
import org.fuck.coolapk.utils.callStaticMethod
import org.fuck.coolapk.utils.findClassOrNull
import org.fuck.coolapk.utils.getInstance
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.hookAfterMethod
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.isNonNull
import org.fuck.coolapk.utils.isNull
import org.fuck.coolapk.utils.log
import org.fuck.coolapk.utils.setObjectField
import org.fuck.coolapk.utils.tryCallMethod
import org.fuck.coolapk.utils.tryOrNull
import org.fuck.coolapk.utils.tryWithXposedLog
import java.lang.reflect.Method
import kotlin.random.Random

class RemoveAds: BaseHook() {

    private val titleRemoveList = listOf(
        "猜你喜欢", // Title过滤
        "优惠券", // Title过滤
        "什么值得买", // Title过滤
        "优选", // Title过滤
        "好物试用", "酷友分享", "的好物"
    )
    private val extraRemoveList = listOf(
        "_AD",
        "_GOODS",
        "酷安小卖部",
        "欢迎分享使用体验",
        "Good.Name",
        "sponsorType",
        "tb.cn"
    )

    private val urlRemoveList = listOf(
        "goods",
        "taobao",
        "tb.cn",
        "jd.com"
    )

    private val templateRemoveList = listOf(
        "sponsorCard",
        "sponsorFeed",
        "sponsorArticleNews",
        "feedDetailReplySponsorCard"
    )

    private val customRules = arrayListOf<CustomRuleConfig.CustomRule>()

    private fun hookFeedGoods() {
        "com.coolapk.market.model.\$\$AutoValue_Feed".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class AutoValue_Feed not found", online = true) }
            .isNonNull { AutoValue_FeedClass ->
                AutoValue_FeedClass.methodFinder()
                    .filterByName("getIncludeGoods")
                    .firstOrNull()
                    .isNull { log("Class AutoValue_Feed Method getIncludeGoods not found") }
                    .isNonNull { it.hookBeforeMethod { it.result = emptyList<Any>() } }
            }
    }

    private fun hookBannerAds() {
        "com.coolapk.market.model.\$\$AutoValue_Feed".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class AutoValue_Feed not found", online = true) }
            .isNonNull { AutoValue_FeedClass->
                AutoValue_FeedClass.methodFinder().filterByName("getDetailSponsorCard")
                    .firstOrNull()
                    .isNull { log("Class AutoValue_Feed Method getDetailSponsorCard not found") }
                    .isNonNull { it.hookBeforeMethod { it.result = null } }
                AutoValue_FeedClass.methodFinder().filterByName("getHotSponsorCard")
                    .firstOrNull()
                    .isNull { log("Class AutoValue_Feed Method getHotSponsorCard not found") }
                    .isNonNull { it.hookBeforeMethod { it.result = null } }
            }
    }

    private fun fuckADSdk() {
//        "com.bytedance.sdk.openadsdk.TTAdSdk".findClassOrNull(CoolContext.classLoader)
//            .isNull { log("Class TTAdSdk not found") }
//            .isNonNull { TTClass ->
//                tryWithXposedLog {
//                    XposedBridge.hookAllMethods(TTClass, "init", XC_MethodReplacement.DO_NOTHING)
//                }
//            }
        "com.bytedance.sdk.openadsdk.TTAdConfig\$Builder".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class TTAdConfig\$Builder not found") }
            .isNonNull { TTClass ->
                TTClass.methodFinder().filterByName("appId")
                    .firstOrNull()
                    .isNull { log("Class TTAdConfig\$Builder Method appId not found") }
                    ?.hookBeforeMethod { param ->
                        param.args[0] = "000000000"
                    }
            }
        "com.qq.e.comm.managers.GDTAdSdk".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class GDTAdSdk not found") }
            .isNonNull { SdkClass ->
                tryWithXposedLog {
                    XposedBridge.hookAllMethods(SdkClass, "init", XC_MethodReplacement.DO_NOTHING)
                }
            }
        "com.kwad.sdk.api.KsAdSDK".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class KsAdSDK not found") }
            .isNonNull { SdkClass ->
                tryWithXposedLog {
                    XposedBridge.hookAllMethods(SdkClass, "init", XC_MethodReplacement.returnConstant(false))
                }
            }
        "com.beizi.ad.BeiZi".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class BeiZi not found") }
            .isNonNull { SdkClass ->
                tryWithXposedLog {
                    XposedBridge.hookAllMethods(SdkClass, "init", XC_MethodReplacement.DO_NOTHING)
                }
            }
        "com.beizi.fusion.BeiZis".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class BeiZis not found") }
            .isNonNull { SdkClass ->
                tryWithXposedLog {
                    XposedBridge.hookAllMethods(SdkClass, "init", XC_MethodReplacement.DO_NOTHING)
                }
            }
        "com.miui.zeus.mimo.sdk.MimoSdk".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class MimoSdk not found") }
            .isNonNull { SdkClass ->
                tryWithXposedLog {
                    XposedBridge.hookAllMethods(SdkClass, "init", XC_MethodReplacement.DO_NOTHING)
                }
            }
        findShouldShowAdMethod()?.hookBeforeMethod {
            log("Set [Method shouldShowAd] result to false")
            it.result = false
        }
    }

    private fun findShouldShowAdMethod(): Method? {
        val cache = CoolContext.dexKitCache.getMethod("ShouldShowAd")
        cache?.let {
            it.getInstance(CoolContext.classLoader)?.let { method ->
                log("loaded Method shouldShowAd from cache")
                return method
            }
            log("Method shouldShowAd from cache cannot get instance")
        }
        val dexKit = CoolContext.dexKit ?: run {
            log("Find shouldShowAd failed, DexKit is null")
            return null
        }
        val result = dexKit.findMethod {
            matcher {
                usingStrings("SPLASH_AD_LAST_SHOW")
                returnType("boolean")
                paramTypes("android.content.Context")
            }
        }.firstOrNull().isNull { log("Method shouldShowAd not found") }
        result?.let {
            return it.getMethodInstance(CoolContext.classLoader).also { CoolContext.dexKitCache.save("ShouldShowAd", it) }
        }
        return null
    }

    private fun getBanner(): Any? {
        try {
            val url = OwnSP.ownSP.getString("bannerUrl", null) ?: "https://github.com/ejiaogl/FuckCoolapk/issues/13"
            val pic = OwnSP.ownSP.getString("bannerPic", null) ?: "https://cdn.jsdelivr.net/gh/ejiaogl/FuckCoolapk@master/art/316/316-2.png"
            return "com.coolapk.market.model.EntityCard".findClassOrNull(CoolContext.classLoader)
                ?.callStaticMethod("builder")
                ?.callMethod("setEntityId", "114514")
                ?.callMethod("setEntityFixed", 1)
                ?.callMethod("setEntityTemplate", "imageCarouselCard_1")
                ?.callMethod("setEntityType", "card")
                ?.callMethod("setExtraData", null)
                ?.callMethod("setEntities", arrayListOf("com.coolapk.market.model.SimpleEntity".findClassOrNull(CoolContext.classLoader)
                    ?.callStaticMethod("builder")
                    ?.callMethod("setEntityType", "image_1")
                    ?.callMethod("setUrl", url)
                    ?.callMethod("setPic", pic)
                    ?.callMethod("build"))
                )?.callMethod("build")
        } catch (e: Exception) {
            log("getBanner = null")
            log(e)
            return null
        }
    }

    private val hasEnableRemoveFeedAds by lazy { OwnSP.ownSP.getBoolean("removeFeedAds", false) }
    private fun shouldFilterAd(entityType: String?, title: String?, extraData: String?, url: String?, entityTemplate: String?, entities: String?): Boolean {
//        log("shouldFilterAd: $entityType, $title, $extraData, $url, $entityTemplate, $entities")
        return when {
            !hasEnableRemoveFeedAds -> false // 激活判断
            entityType?.contains("_goods", ignoreCase = true) ?: false -> true
            titleRemoveList.any { items -> title?.contains(items, ignoreCase = true) ?: false } -> true
            extraRemoveList.any { items -> extraData?.contains(items, ignoreCase = true) ?: false } -> true
            urlRemoveList.any { items -> url?.contains(items, ignoreCase = true) ?: false } -> true
            templateRemoveList.any { items -> entityTemplate?.contains(items, ignoreCase = true) ?: false } -> true
            customRules.any { rule -> rule.satisfy(entityType, title, extraData, url, entityTemplate, entities) } -> true
            else -> false
        }
    }

    private fun filterEntities(entities: List<*>): List<*> {
        val newEntities = mutableListOf<Any>()
//        log("filterEntities.size: ${entities.size}")
        entities.forEach { entity ->
            if (entity == null) return@forEach
            val entityType = entity.callMethod("getEntityType") as String?
            val title = entity.callMethod("getTitle") as String?
            val extraData = entity.callMethod("getExtraData")?.callMethod("toString") as String?
                ?: entity.callMethod("getExtraData") as String?
            val url = entity.callMethod("getUrl") as String?
            val entityTemplate = entity.callMethod("getEntityTemplate") as String?
//            log("filterEntities.entity: $entity")
            if (!shouldFilterAd(entityType, title, extraData, url, entityTemplate, entity.toString())) {
                newEntities.add(entity)
            } else {
                if (entityType == "iconButton") {
                    newEntities.add(entity)
                }
            }
        }
        return newEntities
    }

    private fun shouldFilterFeed(entityType: String?, entity: Any): Boolean {
        if (entityType == "feed") {
            val feedContent = entity.tryCallMethod("getMessage") ?: return false
            return CoolContext.feedFilterRegex.any { (feedContent as String).contains(Regex(it)) }
        }
        return false
    }

    private fun entityFilter() {
        "com.coolapk.market.view.ad.EntityAdHelper".findClassOrNull(CoolContext.classLoader)
            .isNull { log("Class EntityAdHelper not found", online = true) }
            .isNonNull { EntityAdHelperClass->
                EntityAdHelperClass.methodFinder()
                    .filterByParamCount(2)
                    .filterByReturnType(List::class.java)
                    .firstOrNull()
                    .isNull { log("Class EntityAdHelper Method modifyData not found") }
                    .isNonNull { method ->
                        method.hookAfterMethod {
                            val newList = mutableListOf<Any>()
                            loop@ for (item in it.result as List<*>) {
                                if (item == null) continue@loop
                                try {
                                    val entityType = item.callMethod("getEntityType") as String?
                                    val title = item.callMethod("getTitle") as String?
                                    val extraData = item.callMethod("getExtraData")?.callMethod("toString") as String?
                                        ?: item.callMethod("getExtraData") as String?
                                    val url = item.callMethod("getUrl") as String?
                                    val entityTemplate = item.callMethod("getEntityTemplate") as String?
                                    val entities = tryOrNull {
                                        arrayListOf<Any?>().also { list ->
                                            val itemsList = item.callMethod("getEntities") as List<Any?>
                                            list.addAll(itemsList)
                                        }
                                    } ?: arrayListOf()
                                    hasEnable("removeFeedAdsDebug") {
                                        log("----------start removeFeedAdsDebug----------")
                                        log("entityType: $entityType")
                                        log("title: $title")
                                        log("extraData: $extraData")
                                        log("url: $url")
                                        log("entityTemplate: $entityTemplate")
                                        log("entities: $entities")
                                        log("----------end removeFeedAdsDebug----------")
                                    }
                                    if (entityType == "apk") {
                                        newList.add(item)
                                        continue@loop
                                    }
                                    if (entityTemplate == "apkExpandListCard") {
                                        newList.add(item)
                                        continue@loop
                                    }
                                    if (entityType == "feed") {
                                        if (OwnSP.ownSP.getBoolean("blacklist_noFeed", false)) {
                                            try {
                                                val userInfo = item.tryCallMethod("getUserInfo")
                                                if (userInfo != null) {
                                                    val uid = userInfo.tryCallMethod("getUid") as? String
                                                    val username = userInfo.tryCallMethod("getUserName") as? String
                                                    if (uid != null) {
                                                        if (CoolContext.uidBlacklist.contains(uid.toLong())) {
                                                            log("FeedFilter: $username($uid) is in blacklist")
                                                            continue@loop
                                                        }
                                                    }
                                                }
                                            } catch (e: Throwable) {
                                                log(e)
                                            }
                                        }
                                        if (OwnSP.ownSP.getBoolean("topicBlacklist_noFeed", false)) {
                                            try {
                                                val topicList = arrayListOf<String>()
                                                val topic = item.tryCallMethod("getAppName") as? String
                                                if (!topic.isNullOrEmpty()) {
                                                    topicList.add(topic)
                                                }
                                                val relations = item.callMethod("getRelationRows") as? List<*> ?: run {
                                                    log("relations not found")
                                                    null
                                                }
                                                relations?.forEach {relation ->
                                                    val type = relation?.callMethod("getTitle") as? String ?: run {
                                                        log("relation title not found")
                                                        return@forEach
                                                    }
                                                    topicList.add(type)
                                                }
                                                if (topicList.any { CoolContext.topicBlacklist.contains(it) }) {
                                                    log("FeedFilter: $topicList is in blacklist")
                                                    continue@loop
                                                }
                                            } catch (e: Throwable) {
                                                log(e)
                                            }
                                        }
                                        if (OwnSP.ownSP.getBoolean("tagBlacklist_noFeed", false)) {
                                            try {
                                                val tagList = arrayListOf<String>()
                                                val tags = item.callMethod("getTags") as? String ?: run {
                                                    log("tags not found")
                                                    ""
                                                }
                                                tags.split(",").forEach { tag ->
                                                    if (tag.isNotEmpty()) {
                                                        tagList.add(tag)
                                                    }
                                                }
                                                if (tagList.any { CoolContext.tagBlacklist.contains(it) }) {
                                                    log("FeedFilter: $tagList is in blacklist")
                                                    continue@loop
                                                }
                                            } catch (e: Throwable) {
                                                log(e)
                                            }
                                        }
                                    }
                                    if (entityType == "feed_reply") {
                                        if (OwnSP.ownSP.getBoolean("blacklist_noReply", false)) {
                                            try {
                                                val userInfo = item.tryCallMethod("getUserInfo")
                                                if (userInfo != null) {
                                                    val uid = userInfo.tryCallMethod("getUid") as? String
                                                    val username = userInfo.tryCallMethod("getUserName") as? String
                                                    if (uid != null) {
                                                        if (CoolContext.uidBlacklist.contains(uid.toLong())) {
                                                            log("FeedReplyFilter: $username($uid) is in blacklist")
                                                            continue@loop
                                                        }
                                                    }
                                                }
                                            } catch (e: Throwable) {
                                                log(e)
                                            }
                                        }
                                    }
                                    if (entities.isNotEmpty()) {
                                        val newEntities = filterEntities(entities)
                                        item.setObjectField("entities", newEntities)
                                        entities.clear()
                                        entities.addAll(newEntities)
                                        if (entityTemplate == "imageCarouselCard_1" && entities.size == 0) continue@loop
                                    }
                                    val entitiesString = if (entities.isEmpty()) null else entities.toString()
                                    when {
                                        shouldFilterAd(entityType, title, extraData, url, entityTemplate, entitiesString) -> continue@loop
                                        shouldFilterFeed(entityType, item) -> continue@loop
                                        else -> newList.add(item)
                                    }
                                } catch (e: Exception) {
                                    log("Exception when filter ads")
                                    log(e)
                                    newList.add(item)
                                }
                            }
                            // 处理云控卡片
                            if (CoolContext.shouldInsertHeadlineCard &&
                                !CoolContext.insertedHeadlineCard &&
                                (CoolContext.activity.javaClass.name == "com.coolapk.market.view.splash.SplashActivity" || CoolContext.activity.javaClass.name == "com.coolapk.market.view.main.MainActivity")) {
                                getBanner()?.let {
                                    if (newList.size > 3) {
                                        val flag = Random.nextInt(0, newList.size - 3) + 3
                                        newList.add(flag, it)
                                    }
                                }
                                CoolContext.insertedHeadlineCard = true
                            }
                            if (!CoolContext.insertedCustomCard &&
                                (CoolContext.activity.javaClass.name == "com.coolapk.market.view.splash.SplashActivity" || CoolContext.activity.javaClass.name == "com.coolapk.market.view.main.MainActivity")) {
                                CustomCardBuilder.build(OwnSP.ownSP.getString("customCard", "")!!)?.let { card ->
                                    var flag = 0
                                    if (card.random && newList.size > 3) {
                                        flag = Random.nextInt(0, newList.size - 3) + 3
                                    }
                                    card.card.forEach {
                                        newList.add(flag, it)
                                        flag++
                                    }
                                }
                                CoolContext.insertedCustomCard = true
                            }
                            it.result = newList
                        }
                    }
            }
    }

    override fun init() {
        initCustomRules()
        hasEnable("removeBannerAds") { hookBannerAds() } // 横幅广告
        entityFilter() // 过滤器
        hasEnable("removeFeedGoods") { hookFeedGoods() } // 动态提到的好物
        hasEnable("removeFeedAds") { fuckADSdk() }
//        hasEnable("removeFeedAdsEnhance") { hookFeedAdsEnhance() } // 加强版hook已放到HookRecyclerViewHolder
    }

    private fun initCustomRules() {
        customRules.addAll(CoolContext.rulesConfig.getAll())
    }

}
