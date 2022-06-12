package bakamc.chat.common

import bakamc.chat.common.MessageType.WHISPER
import bakamc.chat.common.util.JsonUtil.gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common

 * 文件名 Message

 * 创建时间 2022/6/4 17:48

 * @author forpleuvoir

 */
class Message(
	/**
	 * 消息类型
	 */
	val type: MessageType,
	/**
	 * 发送者信息
	 */
	val sender: PlayerInfo,
	/**
	 * 发送消息的服务器信息
	 */
	val serverInfo: ServerInfo,
	/**
	 * 如果是悄悄话 则为接受者的名称
	 */
	val receiver: String = "",
	/**
	 * 消息内容
	 *
	 * §(content§)为占位符 内容将解析为 text
	 */
	val message: String,
) {
	/**
	 * 发送者的UUID
	 */
	val senderUUID: UUID
		get() = sender.uuid

	/**
	 * 是否为悄悄话
	 */
	val isWhisper: Boolean
		get() = type == WHISPER && receiver.isNotEmpty()

	fun toJson(): JsonObject {
		return gson.toJsonTree(this).asJsonObject
	}

	fun toJsonStr(): String {
		return gson.toJson(this)
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

	override fun toString(): String {
		return "Message(type=$type, sender=$sender, serverInfo=$serverInfo, receiver='$receiver', message='$message')"
	}


}