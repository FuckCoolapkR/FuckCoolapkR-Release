package org.fuck.coolapk.hook

import android.content.ClipData
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.OwnSP
import org.fuck.coolapk.utils.hasEnable
import org.fuck.coolapk.utils.hookBeforeMethod
import org.fuck.coolapk.utils.log
import java.net.URI

class ModifyShareUrl: BaseHook() {
    override fun init() {
        hasEnable("modifyShareUrl") {
            ClipData::class.java.hookBeforeMethod("newPlainText", CharSequence::class.java, CharSequence::class.java) {
                var value = it.args[1] as String
                val regex = Regex("https?://www.coolapk.com/feed/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]")
                val result = regex.findAll(value)
                val redirector = "www.coolapk1s.com"
                result.forEach { matchResult ->
                    log("match = ${matchResult.value}")
                    val query = URI(matchResult.value).rawQuery
                    log("query = $query")
                    value = value.replace(matchResult.value, matchResult.value.replace("www.coolapk.com", redirector).replace(query, ""))
                }
                it.args[1] = value
                log("final = $value")
            }
        }
    }
}
