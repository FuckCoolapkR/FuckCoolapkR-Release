package org.fuck.coolapk.utils

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.widget.Toast
import org.fuck.coolapk.CoolContext
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.random.Random

object RestoreDraft {

    private val draftRequestCode = Random.nextInt(Int.MAX_VALUE)
    private const val FRAGMENT_TAG = "restore_draft_fragment"

    fun start(activity: Activity) {
        val manager = activity.fragmentManager
        manager.findFragmentByTag(FRAGMENT_TAG) as RestoreDraftFragment? ?: run {
            val invisibleFragment = RestoreDraftFragment()
            manager.beginTransaction()
                .add(invisibleFragment, FRAGMENT_TAG)
                .commitNowAllowingStateLoss()
        }
        runCatching {
            manager.findFragmentByTag(FRAGMENT_TAG).isNonNull {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                it.startActivityForResult(intent, draftRequestCode)
            }.isNull { throw NullPointerException("Cannot find fragment") }
        }.onFailure {
            Toast.makeText(CoolContext.context, "无法打开文件管理器", Toast.LENGTH_SHORT).show()
            log(it)
        }
    }

    class RestoreDraftFragment : Fragment() {

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            data ?: return
            val uri = data.data ?: return
            if (requestCode == draftRequestCode && resultCode == Activity.RESULT_OK) {
                CoolContext.context.contentResolver.openInputStream(uri)?.use { steam ->
                    // save file
                    val draftFile = File(CoolContext.context.filesDir.resolve("rough_draft").absolutePath, "rough_draft.bin")
                    runCatching { Files.copy(steam, draftFile.toPath(), StandardCopyOption.REPLACE_EXISTING) }
                        .onSuccess { Toast.makeText(CoolContext.context, "备份恢复成功", Toast.LENGTH_SHORT).show() }
                        .onFailure {
                            Toast.makeText(CoolContext.context, "备份恢复失败", Toast.LENGTH_SHORT).show()
                            log(it)
                        }
                }
            }
        }
    }

}