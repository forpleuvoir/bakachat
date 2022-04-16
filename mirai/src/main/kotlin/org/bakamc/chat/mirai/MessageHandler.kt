package org.bakamc.chat.mirai


import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import org.bakamc.chat.common.message.AbstractMessageHandler
import org.bakamc.chat.common.message.Message
import org.bakamc.chat.common.message.MessageType
import org.bakamc.chat.common.message.MessageType.CHAT
import org.bakamc.chat.common.util.MessageUtil.cleanFormatting

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.mirai

 * 文件名 MessageHandler

 * 创建时间 2022/4/17 2:17

 * @author forpleuvoir

 */
class MessageHandler(val bakacaht: BakaChat) : AbstractMessageHandler<Member>(MessageConfig()) {

	val config: MessageConfig get() = super.messageConfig as MessageConfig

	val bot: Bot get() = Bot.instances.find { it.id == BakaChat.messageHandler.config.bot_id }!!

	override fun reload() {
		TODO("Not yet implemented")
	}

	override fun sendMessage(player: Member, message: String, target: String?, targetId: String?) {
		val senderName = player.nameCard
		val senderUUID = "114514-1919-8100-0000-000000000000"
		val senderServer = messageConfig.sender_server
		sendMessage(
			Message(
				type = if (target != null || targetId != null) MessageType.WHISPER else CHAT,
				receiver_name = target ?: "",
				receiver_uuid = targetId ?: "",
				sender_name = senderName,
				sender_hover_event = messageConfig.sender_name_hover_evnet(senderName, senderUUID),
				sender_click_event = messageConfig.sender_name_click_evnet(senderName, senderUUID),
				sender_uuid = senderUUID,
				server_name = senderServer,
				server_hover_event = messageConfig.server_name_hover_event(senderServer),
				server_click_event = messageConfig.server_name_click_event(senderServer),
				message = message,
				messageHoverEvents = emptyList()
			)
		)
	}

	/**
	 * 发送消息
	 * @param message Message
	 */
	override fun sendMessage(message: Message) {
		parseSendMessage(message).run {
			bakaWSClient.send(this.toJsonStr())
		}
	}

	override fun broadcast(message: Message) {
		for (group in config.groups) {
			bakacaht.launch {
				val msg = StringBuilder()
				msg.append(cleanFormatting(message.server_name))
				msg.append(cleanFormatting(message.sender_name))
				msg.append(cleanFormatting(message.message).replace("[§{].+?(?<=})".toRegex(), "[物品]"))
				bot.getGroup(group)?.sendMessage(msg.toString())
			}
		}
	}

	override fun whisper(message: Message) {
		//悄悄话之后再说
	}
}