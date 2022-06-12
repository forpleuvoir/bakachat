package bakamc.chat.common

import bakamc.chat.common.MessageType.CHAT
import bakamc.chat.common.util.MessageUtil
import java.util.*
import java.util.regex.Pattern


/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common

 * 文件名 AbstractMessageHandler

 * 创建时间 2022/6/5 14:45

 * @author forpleuvoir

 */
abstract class AbstractMessageHandler<T, P>(protected var config: IMessageConfig) : IMessageHandler<T, P> {

	protected val bakaChatWebSocketClient: BakaChatWebSocketClient = BakaChatWebSocketClient(
		this.config.riguruAddress
	) {
		try {
			receivesMessage(Message.fromJsonStr(it))
		} catch (e: Exception) {
			println("消息解析失败")
			e.printStackTrace()
		}
	}

	val messageHandlers: List<(Message, P) -> Message> = LinkedList<(Message, P) -> Message>().apply {
		//处理格式
		add { msg, _ ->
			Message(msg.type, msg.sender, msg.serverInfo, msg.receiver, MessageUtil.handleFormat(msg.message))
		}
		//处理@格式
		add { msg, _ ->
			Message(msg.type, msg.sender, msg.serverInfo, msg.receiver, msg.message.replace(Regex("@.+?(?<=\\b)")) { result ->
				"§b${result.value}§r"
			})
		}
		//处理物品展示
		add(this@AbstractMessageHandler::handleItemShow)
	}

	override fun connect() = bakaChatWebSocketClient.connect()

	override fun reconnect() = bakaChatWebSocketClient.reconnect()

	override fun close() = bakaChatWebSocketClient.close()

	override val isOpen: Boolean get() = bakaChatWebSocketClient.isOpen

	override fun serverInfo(): ServerInfo = config.serverInfo

	override fun sendMessage(player: P, message: String) {
		sendMessageToRiguru(Message(CHAT, player.playerInfo(), serverInfo(), message = message).messagePreHandle(player))
	}

	override fun sendMessageToRiguru(message: Message) {
		bakaChatWebSocketClient.send(message.toJsonStr())
	}

	override fun receivesMessage(message: Message) {
		if (message.isWhisper) {
			whisper(message)
		} else {
			broadcast(message)
		}
	}

	override fun Message.messagePreHandle(player: P): Message {
		var msg: Message = this
		messageHandlers.forEach {
			try {
				msg = it(msg, player)
			} catch (e: Exception) {
				println("消息处理程序出错")
				e.printStackTrace()
			}
		}
		return msg
	}

	/**
	 * 获取当前消息@的玩家名
	 * @param message Message
	 * @return List<String>
	 */
	override fun Message.getAtList(): List<String> {
		val list = LinkedList<String>()
		val regex = "@.+?\\b"
		val pattern = Pattern.compile(regex)
		val matcher = pattern.matcher(this.message)
		while (matcher.find()) {
			list.add(matcher.group())
		}
		return list
	}

	/**
	 * 将[Message.message]转换为带json格式的物品信息Text的字符串  §({text})
	 * @param message Message
	 * @return Message
	 */
	open fun handleItemShow(message: Message, player: P): Message {
		val regex = Regex("%(\\d|i|o)")
		val msg = message.message.replace(regex) { result ->
			val indexChar = result.value[1].toString()
			val index = if (indexChar == "i") -1 else if (indexChar == "o") -2 else indexChar.toInt() - 1
			"§(${player.getItemJson(index)}§)"
		}
		return Message(message.type, message.sender, message.serverInfo, message.receiver, msg)
	}

	/**
	 * 将[index]位置的物品转换为Text [index]=-1时应该返回主手物品 [index]=-2返回副手的物品
	 * @param index Int
	 * @param player P
	 * @return String
	 */
	abstract fun P.getItemJson(index: Int): String
}