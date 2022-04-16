package org.bakamc.chat.common.message

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.bakamc.chat.common.message.textevent.ClickEvent
import org.bakamc.chat.common.message.textevent.HoverEvent
import org.bakamc.chat.common.util.JsonUtil.gson
import java.util.*

/**
 * 消息

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.message

 * 文件名 Message

 * 创建时间 2022/4/10 12:15

 * @author forpleuvoir

 */
class Message(
	/**
	 * 消息类型
	 */
	val type: MessageType,
	/**
	 * 如果是悄悄话则为目标的名称
	 */
	val receiver_name: String = "",
	/**
	 * 如果是悄悄话则为目标的uuid字符串
	 */
	val receiver_uuid: String = "",
	/**
	 * 发送者名称
	 */
	val sender_name: String,
	/**
	 * 发送者文本悬浮事件
	 */
	val sender_hover_event: HoverEvent? = null,
	/**
	 * 发送者文本点击事件
	 */
	val sender_click_event: ClickEvent? = null,
	/**
	 * 发送者uuid字符串
	 */
	val sender_uuid: String,
	/**
	 * 发送消息的服务器
	 */
	val server_name: String,
	/**
	 * 服务器文本悬浮事件
	 */
	val server_hover_event: HoverEvent? = null,
	/**
	 * 服务器文本点击事件
	 */
	val server_click_event: ClickEvent? = null,
	/**
	 * 消息内容 事件占位符 §{}
	 *
	 * 例:'呃啊§{Baka}§{傻子}'
	 *
	 * 会给'Baka'添加 messageHoverEvents.get(0) 的事件
	 *
	 * '傻子' = messageHoverEvents.get(1)
	 */
	val message: String,
	/**
	 * 消息内容悬浮事件
	 */
	val messageHoverEvents: List<HoverEvent> = LinkedList(),
) {

	/**
	 * 发送者UUID
	 */
	val senderUUID: UUID
		get() = try {
			UUID.fromString(sender_uuid)
		} catch (e: Exception) {
			UUID.fromString("00000000-0000-0000-0000-000000000000")
		}

	/**
	 * 悄悄话接收者UUID
	 */
	val receiverUUID: UUID
		get() = try {
			UUID.fromString(receiver_uuid)
		} catch (e: Exception) {
			UUID.fromString("00000000-0000-0000-0000-000000000000")
		}

	fun toJson(): JsonObject {
		return gson.toJsonTree(this).asJsonObject
	}

	fun toJsonStr(): String {
		return gson.toJson(this)
	}

	override fun toString(): String {
		return "Message(type=$type, receiver_name='$receiver_name', receiver_uuid='$receiver_uuid', sender_name='$sender_name', sender_hover_event=$sender_hover_event, sender_click_event=$sender_click_event, sender_uuid='$sender_uuid', sender_server='$server_name', server_hover_event=$server_hover_event, server_click_event=$server_click_event, message='$message', messageHoverEvents=$messageHoverEvents)"
	}


	companion object {

		@JvmStatic
		fun fromJsonStr(json: String): Message {
			return gson.fromJson(json, Message::class.java)
		}

		@JvmStatic
		fun fromJson(json: JsonElement): Message {
			return gson.fromJson(json, Message::class.java)
		}
	}
}

