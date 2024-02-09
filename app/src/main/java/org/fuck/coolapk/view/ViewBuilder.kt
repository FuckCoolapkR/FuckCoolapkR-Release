package org.fuck.coolapk.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import org.fuck.coolapk.utils.*

class ViewBuilder(private val context: Context) {

    fun text(text: String, textSize: Float? = null, textColor: Int? = null, onClickListener: ((TextView) -> Unit)? = null): View {
        return TextView(context).also { view ->
            view.text = text
            textSize.isNull { view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f) }.isNonNull { view.setTextSize(TypedValue.COMPLEX_UNIT_SP, it) }
            textColor.isNull {  view.setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000")) }.isNonNull { view.setTextColor(it) }
            onClickListener.isNonNull { block ->
                view.setOnClickListener { block(view) }
            }
            view.setPadding(0, dp2px(context, 5f), 0, dp2px(context, 5f))
        }
    }

    fun editText(hint: String? = null, callback: (String) -> Unit): EditText {
        return EditText(context).also { view ->
            hint?.let { view.hint = it }
            view.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable) { callback(p0.toString()) }
            })
            view.setPadding(0, dp2px(context, 5f), 0, dp2px(context, 5f))
        }
    }


    fun button(text: String? = null, onClickListener: View.OnClickListener? = null): Button {
        return Button(context).also { view ->
            text?.let { view.text = it }
            onClickListener?.let { view.setOnClickListener(it) }
            view.setPadding(0, dp2px(context, 5f), 0, dp2px(context, 5f))
        }
    }

    fun textWithSwitch(
        text: String,
        key: String,
        defaultValue: Boolean = false,
        textSize: Float? = null,
        textColor: Int? = null,
        onClickListener: ((TextView) -> Unit)? = null,
        onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null,
        doubleConfirm: Boolean = false,
        message: String = "该功能实验中，尚不稳定，请谨慎使用"
    ): View {
        val textView = text(text, textSize, textColor, onClickListener)
        val switch = Switch(context).also { view ->
            view.isChecked = OwnSP.ownSP.getBoolean(key, defaultValue)
            view.setOnCheckedChangeListener { compoundButton, b ->
                onCheckedChangeListener?.onCheckedChanged(compoundButton, b)
                OwnSP.ownSP.edit().run {
                    putBoolean(key, b)
                    apply()
                }
                if (doubleConfirm && b) {
                    getDialogBuilder(context).apply {
                        setTitle("警告")
                        setMessage(message)
                        setPositiveButton("我已知晓", null)
                        setNegativeButton("取消启用") {_, _ -> compoundButton.isChecked = false}
                        setOnCancelListener { compoundButton.isChecked = false }
                        setCancelable(false)
                        show()
                    }
                }
            }
        }
        return LinearLayout(context).also { view ->
            view.addView(textView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            view.addView(switch, LinearLayout.LayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL }))
        }
    }

}