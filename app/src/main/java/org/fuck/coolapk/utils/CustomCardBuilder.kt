package org.fuck.coolapk.utils

import org.fuck.coolapk.Config
import org.fuck.coolapk.CoolContext
import org.json.JSONArray
import org.json.JSONObject

object CustomCardBuilder {

    private fun buildExtraData(string: String): Any? {
        try {
            val clazz = "com.coolapk.market.model.ExtraData".findClassOrNull(CoolContext.classLoader) ?: return null
            val stringConstructor = clazz.getDeclaredConstructor(String::class.java)
            stringConstructor.isAccessible = true
            return stringConstructor.newInstance(string)
        } catch (e: Throwable) {
            log(e)
            return null
        }
    }

    private fun buildEntities(jsonArray: JSONArray): List<Any> {
        val clazz = "com.coolapk.market.model.SimpleEntity".findClassOrNull(CoolContext.classLoader) ?: return emptyList()
        clazz.callStaticMethodOrNull("builder") ?: return emptyList()
        val list = mutableListOf<Any>()
        jsonArray.forEach { any ->
            val jsonObject = any as? JSONObject ?: return@forEach
            val dateline = tryOrNull { jsonObject.getLong("dateline") }
            val description = tryOrNull { jsonObject.getString("description") }
            val entityFixed = tryOrNull { jsonObject.getInt("entityFixed") }
            val entityId = tryOrNull { jsonObject.getString("entityId") }
            val entityTemplate = tryOrNull { jsonObject.getString("entityTemplate") }
            val entityType = tryOrNull { jsonObject.getString("entityType") }
            val entityTypeName = tryOrNull { jsonObject.getString("entityTypeName") }
            val extraData = tryOrNull { jsonObject.getString("extraData")?.let { buildExtraData(it) } }
            val id = tryOrNull { jsonObject.getString("id") }
            val lastUpdate = tryOrNull { jsonObject.getLong("lastUpdate") }
            val logo = tryOrNull { jsonObject.getString("logo") }
            val pic = tryOrNull { jsonObject.getString("pic") }
            val subTitle = tryOrNull { jsonObject.getString("subTitle") }
            val title = tryOrNull { jsonObject.getString("title") }
            val url = tryOrNull { jsonObject.getString("url") }
            val simpleEntityBuilder = clazz.callStaticMethodOrNull("builder")!!
            simpleEntityBuilder.apply {
                dateline?.let { tryCallMethod("setDateLine", it) }
                description?.let { tryCallMethod("setDescription", it) }
                entityFixed?.let { tryCallMethod("setEntityFixed", it) }
                entityId?.let { tryCallMethod("setEntityId", it) }
                entityTemplate?.let { tryCallMethod("setEntityTemplate", it) }
                entityType?.let { tryCallMethod("setEntityType", it) }
                entityTypeName?.let { tryCallMethod("setEntityTypeName", it) }
                extraData?.let { tryCallMethod("setExtraData", it) }
                id?.let { tryCallMethod("setId", it) }
                lastUpdate?.let { tryCallMethod("setLastUpdate", it) }
                logo?.let { tryCallMethod("setLogo", it) }
                pic?.let { tryCallMethod("setPic", it) }
                subTitle?.let { tryCallMethod("setSubTitle", it) }
                title?.let { tryCallMethod("setTitle", it) }
                url?.let { tryCallMethod("setUrl", url) }
            }
            simpleEntityBuilder.tryCallMethod("build")?.let { list.add(it) }
        }
        return list
    }

    private fun parseCard(jsonObject: JSONObject): Any? {
        val dateline = tryOrNull { jsonObject.getLong("dateline") }
        val description = tryOrNull { jsonObject.getString("description") }
        val entityFixed = tryOrNull { jsonObject.getInt("entityFixed") }
        val entityId = tryOrNull { jsonObject.getString("entityId") }
        val entityTemplate = tryOrNull { jsonObject.getString("entityTemplate") }
        val entityType = tryOrNull { jsonObject.getString("entityType") }
        val entityTypeName = tryOrNull { jsonObject.getString("entityTypeName") }
        val extraData = tryOrNull { jsonObject.getString("extraData")?.let { buildExtraData(it) } }
        val id = tryOrNull { jsonObject.getString("id") }
        val lastUpdate = tryOrNull { jsonObject.getLong("lastUpdate") }
        val logo = tryOrNull { jsonObject.getString("logo") }
        val pic = tryOrNull { jsonObject.getString("pic") }
        val subTitle = tryOrNull { jsonObject.getString("subTitle") }
        val title = tryOrNull { jsonObject.getString("title") }
        val url = tryOrNull { jsonObject.getString("url") }
        val entities = tryOrNull { jsonObject.getJSONArray("entities")?.let { buildEntities(it) } }
        val entityCardClass = "com.coolapk.market.model.EntityCard".findClassOrNull(CoolContext.classLoader) ?: return null
        val entityCardBuilder = entityCardClass.callStaticMethodOrNull("builder") ?: return null
        entityCardBuilder.apply {
            dateline?.let { tryCallMethod("setDateLine", it) }
            description?.let { tryCallMethod("setDescription", it) }
            entityFixed?.let { tryCallMethod("setEntityFixed", it) }
            entityId?.let { tryCallMethod("setEntityId", it) }
            entityTemplate?.let { tryCallMethod("setEntityTemplate", it) }
            entityType?.let { tryCallMethod("setEntityType", it) }
            entityTypeName?.let { tryCallMethod("setEntityTypeName", it) }
            extraData?.let { tryCallMethod("setExtraData", it) }
            id?.let { tryCallMethod("setId", it) }
            lastUpdate?.let { tryCallMethod("setLastUpdate", it) }
            logo?.let { tryCallMethod("setLogo", it) }
            pic?.let { tryCallMethod("setPic", it) }
            subTitle?.let { tryCallMethod("setSubTitle", it) }
            title?.let { tryCallMethod("setTitle", it) }
            url?.let { tryCallMethod("setUrl", url) }
            entities?.let { tryCallMethod("setEntities", it) }
        }
        return entityCardBuilder.tryCallMethod("build")
    }

    fun build(string: String): Card? {
        if (string.isEmpty()) return null
        val decoded = ContentEncodeUtils.decode(string) ?: return null
        val card = tryOrNull { JSONObject(decoded).getJSONArray("card") } ?: return null
        val version = tryOrNull { JSONObject(decoded).getString("version") }
        val random = tryOrNull { JSONObject(decoded).getBoolean("random") } ?: false
        if (version != Config.cardBuilderVersion) return null
        val list = mutableListOf<Any>()
        card.forEach { any ->
            val jsonObject = any as? JSONObject ?: return@forEach
            parseCard(jsonObject)?.let { list.add(it) }
        }
        return Card(list, random)
    }

    data class Card(val card: List<Any>, val random: Boolean)

}