package org.fuck.coolapk.hook

import android.view.View
import android.widget.EditText
import android.widget.TextView
import de.robv.android.xposed.XposedBridge
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.*

class FeedPostChecker: BaseHook() {

    private val hardcode = "发布"

    private val filterKeywords by lazy {
        (OwnSP.ownSP.getString("wlimits", "")).apply {
            if (this == null) {
                return@lazy listOf<String>()
            }
            if (this.isNotBlank()) {
                return@lazy (tryOrNull { ContentEncodeUtils.decode(this) } ?: "")
                    .split(";;")
                    .filter { it.isNotEmpty() }
            }
        }
        return@lazy listOf<String>()
    }

    override fun init() {
        hookReply()
        hookForward()
    }

    private fun hookForward() {
        "com.coolapk.market.view.feed.ForwardEntityActivity".hookBeforeMethod(CoolContext.classLoader, "onClick", View::class.java) { check(it) }
    }

    private fun hookReply() {
        "com.coolapk.market.view.feed.ReplyActivity".hookBeforeMethod(CoolContext.classLoader, "onClick", View::class.java) { check(it) }
    }

    private fun check(param: MethodHookParam) {
        runCatching {
            val view = param.args[0] as View
            if (view is TextView && view.text == hardcode) {
                log("Current filter keywords: $filterKeywords")
                val bindings = param.thisObject.javaClass.declaredFields.filter {
                    it.type.superclass != null && it.type.superclass.name == "androidx.databinding.ViewDataBinding"
                }
                bindings.forEach { bf ->
                    log("FeedPostChecker: (bf): $bf")
                    bf.isAccessible = true
                    val dataBinding = bf.get(param.thisObject)
                    dataBinding.javaClass.fields.forEach {
                        log("FeedPostChecker: (bf): $bf, it: $it")
                    }
                    val findViews = dataBinding.javaClass.fields.filter { vf -> vf.type == EditText::class.java }
                    if (findViews.isEmpty()) {
                        log("FeedPostChecker: Can't find EditText in view binding!", true)
                        XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args)
                        return@forEach
                    }
                    if (findViews.size > 1) {
                        log("FeedPostChecker: Too many EditText in view binding!", true)
                        XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args)
                        return@forEach
                    }
                    val editText = findViews[0].apply{ isAccessible = true }.get(dataBinding) as EditText
                    log("FeedPostChecker: (editText): ${editText.text}")
                    val results = filterKeywords.filter { editText.text.toString().trim().contains(it) }
                    if (results.isNotEmpty()) {
                        log("FeedPostChecker: keywords detected: $results")
                        getDialogBuilder(view.context)
                            .setTitle("警告")
                            .setMessage("检测到敏感词: $results\n是否继续发布？\n*发布包含敏感词的内容可能会导致黑号")
                            .setPositiveButton("继续") { _, _ ->
                                XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args)
                            }
                            .setNegativeButton("取消") { _, _ -> }
                            .show()
                        param.result = null
                    }
                }
            }
        }.onFailure { log(it) }
    }

}