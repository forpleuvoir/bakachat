package org.bakamc.chat.common.message.textevent

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.message.textevent

 * 文件名 HoverEventAdapter

 * 创建时间 2022/4/12 22:10

 * @author forpleuvoir

 */
class HoverEventAdapter : JsonDeserializer<HoverEvent> {

	override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HoverEvent? {
		if (json == null || !json.isJsonObject) return null
		else {
			try {
				json.asJsonObject.let {
					val action = HoverEvent.Action.valueOf(it.get("action").asString)
					val content = it.getAsJsonObject("content").let { obj ->
						when (action) {
							HoverEvent.Action.SHOW_TEXT   -> HoverEvent.TextContent(ArrayList<String>().apply {
								obj.get("texts").asJsonArray.forEach { element ->
									add(
										element.asString
									)
								}
							})
							HoverEvent.Action.SHOW_ITEM   -> HoverEvent.ItemContent(
								obj.get("id").asString,
								obj.get("count").asInt,
								obj.get("tag").asString,
								obj.get("rarity").asString[0]
							)
							HoverEvent.Action.SHOW_ENTITY -> HoverEvent.EntityContent(
								obj.get("type").asString,
								obj.get("id").asString,
								obj.get("name").asString
							)
						}
					}
					return HoverEvent(content)
				}
			} catch (_: Exception) {
			}
			return null
		}
	}
}