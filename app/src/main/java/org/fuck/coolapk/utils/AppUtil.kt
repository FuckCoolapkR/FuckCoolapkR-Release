package org.fuck.coolapk.utils

import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.widget.LinearLayout
import android.widget.ScrollView

fun dp2px(context: Context, dpValue: Float): Int = (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()
fun isNightMode(context: Context): Boolean = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

fun getDialogBuilder(context: Context) : AlertDialog.Builder {
    return if (isNightMode(context)) {
        AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
    } else {
        AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert)
    }
}

inline fun AlertDialog.Builder.setScrollView(context: Context, crossinline block: LinearLayout.() -> Unit): AlertDialog.Builder {
    setView(ScrollView(context).apply {
        overScrollMode = 2
        addView(LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp2px(context, 20f), dp2px(context, 10f), dp2px(context, 20f), dp2px(context, 5f))
            block(this)
        })
    })
    return this
}