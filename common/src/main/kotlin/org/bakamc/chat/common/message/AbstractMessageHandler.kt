package org.bakamc.chat.common.message

import org.bakamc.chat.common.util.MessageUtil.handleFormat
import org.bakamc.chat.common.websocket.BakaWSClient
import java.util.regex.Pattern

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.message

 * 文件名 AbstractMessageHandler

 * 创建时间 2022/4/10 19:01

 * @author forpleuvoir

 */
abstract class AbstractMessageHandler<T>(var messageConfig: IMessageConfig) {

	protected val bakaWSClient: BakaWSClient = BakaWSClient(messageConfig.riguru_address) {
		try {
			receivesMessage(Message.fromJsonStr(it))
		} catch (e: Exception) {
			println("消息解析失败")
			e.printStackTrace()
		}
	}

	fun connect() = bakaWSClient.connect()

	fun reconnect() = bakaWSClient.reconnect()

	fun close() = bakaWSClient.close()

	val isOpen get() = bakaWSClient.isOpen

	abstract fun reload()

	abstract fun sendMessage(player: T, message: String, target: String? = null, targetId: String? = null)

	/**
	 * 发送消息
	 * @param message Message
	 */
	open fun sendMessage(message: Message) {
		parseSendMessage(message).run {
			bakaWSClient.send(this.toJsonStr())
			receivesMessage(this)
		}
	}

	/**
	 * 处理接受到的消息 包括把消息广播到服务器 或者发送悄悄话
	 * @param message Message
	 */
	open fun receivesMessage(message: Message) {
		when (message.type) {
			MessageType.CHAT    -> broadcast(message)
			MessageType.WHISPER -> whisper(message)
		}
	}

	/**
	 * 处理消息占位符
	 * @param message Message
	 * @return Message
	 */
	open fun parseSendMessage(message: Message): Message {
		return Message(
			type = message.type,
			receiver_name = message.receiver_name,
			receiver_uuid = message.receiver_uuid,
			sender_name = messageConfig.sender_name_exp(message.sender_name),
			sender_hover_event = message.sender_hover_event,
			sender_click_event = message.sender_click_event,
			sender_uuid = message.sender_uuid,
			server_name = message.server_name,
			server_hover_event = message.server_hover_event,
			server_click_event = message.server_click_event,
			message = messageConfig.message_exp(handleFormat(message.message)),
			messageHoverEvents = message.messageHoverEvents
		)
	}

	/**
	 * 处理At文本
	 * @param msg String
	 * @param list MutableList<String> 存放被at的玩家名称
	 * @return String
	 */
	protected fun handlerAt(msg: String, list: MutableList<String>): String {
		val regex = "(?<=@).+?(?<=\\b)"
		val pattern = Pattern.compile(regex)
		val matcher = pattern.matcher(msg)
		while (matcher.find()) {
			list.add(matcher.group())
		}
		return msg.replace(Regex("@.+?(?<=\\b)")) {
			"§b${it.value}§r"
		}
	}

	/**
	 * 向服务器广播消息
	 * @param message Message
	 */
	abstract fun broadcast(message: Message)

	/**
	 * 向指定玩家发送悄悄话
	 * @param message Message
	 */
	abstract fun whisper(message: Message)

}