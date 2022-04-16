package org.bakamc.chat.common.message.textevent

import org.bakamc.chat.common.util.JsonUtil.gson

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.message.textevent

 * 文件名 HoverEvent

 * 创建时间 2022/4/10 13:25

 * @author forpleuvoir

 */
class HoverEvent(val content: Content) : TextEvent() {

	val action: Action

	init {
		action = when (content::class.java) {
			ItemContent::class.java -> Action.SHOW_ITEM
			TextContent::class.java -> Action.SHOW_TEXT
			else                    -> Action.SHOW_ENTITY
		}
	}

	fun toJsonStr(): String = gson.toJson(this)

	override fun toString(): String {
		return "HoverEvent(action=$action, content=$content)"
	}

	enum class Action {
		SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY
	}

	open class Content

	class ItemContent(val id: String, val count: Int, val tag: String, val rarity: Char = 'r') : Content() {

		override fun toString(): String {
			return "ItemContent(id='$id', count=$count, tag='$tag', rarity='$rarity')"
		}
	}

	class TextContent(val texts: List<String>) : Content() {

		override fun toString(): String {
			return "TextContent(texts=$texts)"
		}
	}

	class EntityContent(val type: String = "minecraft:pig", val id: String, val name: String) : Content() {

		override fun toString(): String {
			return "EntityContent(type='$type', id='$id', name='$name')"
		}
	}

}




