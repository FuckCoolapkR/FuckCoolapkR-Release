package org.fuck.coolapk.config

import android.content.Context
import io.fastkv.FastKV
import io.fastkv.interfaces.FastEncoder
import io.packable.PackArrayCreator
import io.packable.PackDecoder
import io.packable.PackEncoder
import io.packable.Packable
import org.fuck.coolapk.CoolContext
import org.fuck.coolapk.utils.log

class CustomRuleConfig(context: Context, name: String) {

    private val key = "rules"

    private val kv: FastKV
    private val theList = arrayListOf<CustomRule>()
    init {
        val path = context.filesDir.absolutePath + "/fc"
        kv = FastKV.Builder(path, name)
            .encoder(arrayOf(CustomRuleListEncoder.INSTANCE))
            .build()
        val data : List<CustomRule> = kv.getObject(key) ?: arrayListOf()
        theList.addAll(data)
    }

    val size: Int
        get() {
            synchronized(theList) {
                return theList.size
            }
        }

    fun add(rule: CustomRule) {
        synchronized(theList) {
            theList.add(rule)
            kv.putObject(key, theList, CustomRuleListEncoder.INSTANCE)
        }
    }

    fun replaceAll(list: List<CustomRule>) {
        synchronized(theList) {
            theList.clear()
            theList.addAll(list)
            kv.putObject(key, theList, CustomRuleListEncoder.INSTANCE)
        }
    }

    fun remove(rule: CustomRule) {
        synchronized(theList) {
            theList.remove(rule)
            kv.putObject(key, theList, CustomRuleListEncoder.INSTANCE)
        }
    }

    fun clear() {
        synchronized(theList) {
            theList.clear()
            kv.clear()
        }
    }

    fun getAll(): List<CustomRule> {
        synchronized(theList) {
            log("CustomRuleConfig.getAll() theList = $theList")
            return theList
        }
    }

    fun contains(rule: CustomRule): Boolean {
        synchronized(theList) {
            return theList.contains(rule)
        }
    }

    companion object {
        val default by lazy { CustomRuleConfig(CoolContext.context, "cr") }
    }

    private class CustomRuleListEncoder: FastEncoder<List<CustomRule>> {
        override fun tag(): String = "CustomRuleList"

        override fun encode(obj: List<CustomRule>): ByteArray {
            return PackEncoder().putPackableList(0, obj).bytes
        }

        override fun decode(bytes: ByteArray, offset: Int, length: Int): List<CustomRule> {
            return PackDecoder.newInstance(bytes, offset, length).getPackableList(0, CustomRule.CREATOR)
        }

        companion object {
            val INSTANCE = CustomRuleListEncoder()
        }

    }

    data class CustomRule(
        val entityType: String?,
        val title: String?,
        val extraData: String?,
        val url: String?,
        val entityTemplate: String?,
        val entities: String?
    ): Packable {

        fun satisfy(eTy: String?, t: String?, eD: String?, u: String?, eTe: String?, e: String?) : Boolean {
            return checkEntityType(eTy) && checkTitle(t) && checkExtraData(eD) && checkUrl(u) && checkEntityTemplate(eTe) && checkEntities(e)
        }

        private fun checkEntityType(eTy: String?): Boolean {
            entityType ?: return true
            return eTy?.contains(entityType) ?: false
        }

        private fun checkTitle(t: String?): Boolean {
            title ?: return true
            return t?.contains(title) ?: false
        }

        private fun checkExtraData(eD: String?): Boolean {
            extraData ?: return true
            return eD?.contains(extraData) ?: false
        }

        private fun checkUrl(u: String?): Boolean {
            url ?: return true
            return u?.contains(url) ?: false
        }

        private fun checkEntityTemplate(eTe: String?): Boolean {
            entityTemplate ?: return true
            return eTe?.contains(entityTemplate) ?: false
        }

        private fun checkEntities(e: String?): Boolean {
            entities ?: return true
            return e?.contains(entities) ?: false
        }

        override fun encode(encoder: PackEncoder) {
            encoder.putString(0, entityType)
            encoder.putString(1, title)
            encoder.putString(2, extraData)
            encoder.putString(3, url)
            encoder.putString(4, entityTemplate)
            encoder.putString(5, entities)
        }

        companion object {
            val CREATOR = object: PackArrayCreator<CustomRule> {
                override fun decode(decoder: PackDecoder): CustomRule {
                    return CustomRule(
                        decoder.getString(0),
                        decoder.getString(1),
                        decoder.getString(2),
                        decoder.getString(3),
                        decoder.getString(4),
                        decoder.getString(5)
                    )
                }

                override fun newArray(size: Int): Array<CustomRule?> {
                    return arrayOfNulls(size)
                }
            }
        }

    }
}