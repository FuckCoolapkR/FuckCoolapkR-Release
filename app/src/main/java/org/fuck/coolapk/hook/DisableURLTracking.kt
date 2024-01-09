package org.fuck.coolapk.hook

import android.net.Uri
import org.fuck.coolapk.base.BaseHook
import org.fuck.coolapk.utils.hookAfterMethod

class DisableURLTracking: BaseHook() {

    override fun init() {
        Uri.Builder::class.java.hookAfterMethod("build") { param ->
            val uri = param.result as Uri? ?: return@hookAfterMethod
            val link = uri.toString()
            if (link.contains("coolapk.com/link")) {
                val realLink = uri.getQueryParameter("url") ?: return@hookAfterMethod
                val newUri = Uri.parse(realLink)
                param.result = newUri
            }
        }
    }

}