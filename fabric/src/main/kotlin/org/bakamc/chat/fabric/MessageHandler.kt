package org.bakamc.chat.fabric

import bakamc.chat.common.message.*
import bakamc.chat.common.message.MessageType.CHAT
import net.minecraft.item.ItemStack
import net.minecraft.network.encryption.NetworkEncryptionUtils.SignatureData
import net.minecraft.network.message.MessageSender
import net.minecraft.network.message.MessageType
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.*
import net.minecraft.text.ClickEvent.Action.SUGGEST_COMMAND
import net.minecraft.text.HoverEvent.Action
import net.minecraft.text.HoverEvent.ItemStackContent
import net.minecraft.util.Formatting.GRAY
import net.minecraft.util.Formatting.ITALIC
import org.bakamc.chat.fabric.mixin.MixinServerPlayerEntity
import org.bakamc.chat.fabric.util.text
import java.time.Instant
import java.util.*
import java.util.regex.Pattern

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric

 * 文件名 MessageHandler

 * 创建时间 2022/6/12 13:00

 * @author forpleuvoir

 */
class MessageHandler(config: IMessageConfig, private val server: MinecraftServer) : AbstractMessageHandler<Text, ServerPlayerEntity>(config) {

	override fun ServerPlayerEntity.getItemJson(index: Int): String {
		val stack = if (index == -1) this.mainHandStack else if (index == -2) this.offHandStack else inventory.getStack(index)
		return Text.Serializer.toJson(stack.toHoverAbleText())
	}

	private fun ItemStack.toHoverAbleText(): Text {
		val mutableText: MutableText = Text.empty().append(this.name).append(if (this.count > 1) " x${this.count}" else "")
		if (this.hasCustomName()) {
			mutableText.formatted(ITALIC)
		}
		val mutableText2 = MutableText.of(LiteralTextContent("§b[§r")).append(mutableText).append("§b]§r")
		if (!this.isEmpty) {
			mutableText2.formatted(this.rarity.formatting).styled { style: Style ->
				style.withHoverEvent(HoverEvent(Action.SHOW_ITEM, ItemStackContent(this)))
			}
		}
		return mutableText2
	}

	override fun reload() {
		TODO("Not yet implemented")
	}

	override fun Message.toText(): Text {
		val regex = "§\\(.+?§\\)"
		val texts = ArrayList<Text>()
		val message = riguruMessageConfig.messageWrapper.replace("%message%", message)
		Pattern.compile(regex).matcher(message).run {
			while (this.find()) {
				texts.add(Text.Serializer.fromJson(this.group())!!)
			}
		}
		val msg: MutableText = Text.empty()
		this.message.split(regex.toRegex()).run {
			forEachIndexed { index, str ->
				msg.append(str).let {
					if (index != this.size - 1) it.append(texts[index])
				}
			}
		}
		return msg
	}

	override fun ServerInfo.toText(): Text {
		val hoverText = MutableText.of(LiteralTextContent("${riguruMessageConfig.serverWrapper.replace("%serverName%", serverName)}\n"))
			.append("\n")
			.append("服务器ID:$serverId\n")
			.append("$description\n")
			.append("\n")
			.append("点击加入此服务器")
		return MutableText.of(LiteralTextContent("$serverName\n")).styled {
			it.withHoverEvent(HoverEvent(Action.SHOW_TEXT, hoverText))
			it.withClickEvent(ClickEvent(SUGGEST_COMMAND, "/server $serverId"))
		}
	}

	override fun PlayerInfo.toText(): Text {
		val hoverText = MutableText.of(LiteralTextContent("${
			riguruMessageConfig.playerNameWrapper.replace("%playerName%", name).replace("%displayName%", displayName)
		}\n"))
			.append("\n")
			.append("玩家名:$displayName\n")
			.append("uuid:$uuid\n")
			.append("等级:$level\n")
			.append("总经验值:$experience\n")
			.append("生命值:$health/$maxHealth\n")
			.append("所在世界:$dimension")
			.append("\n")
			.append("点击与该玩家私聊")
		return MutableText.of(LiteralTextContent("$name\n")).styled {
			it.withHoverEvent(HoverEvent(Action.SHOW_TEXT, hoverText))
			it.withClickEvent(ClickEvent(SUGGEST_COMMAND, "/bakachat:msg $name"))
		}
	}

	override fun ServerPlayerEntity.playerInfo(): PlayerInfo {
		return PlayerInfo(
			this.entityName,
			this.displayName.string,
			this.uuid,
			this.experienceLevel,
			this.totalExperience,
			this.maxHealth,
			this.health,
			this.world.registryKey.value.toString()
		)
	}

	override fun Message.toFinalBroadcastText(): Text {
		val text = Text.empty()
		riguruMessageConfig.chatSort.forEach {
			when (it) {
				"server"     -> {
					text.append(serverInfo.toText())
				}
				"prefix"     -> {
					// TODO: 2022/6/15
				}
				"playerName" -> {
					text.append(sender.toText())
				}
				"message"    -> {
					text.append(toText())
				}
			}
		}
		return text
	}

	override fun Message.toFinalWhisperText(player: ServerPlayerEntity): Text {
		val isSender = sender.name == player.entityName || sender.displayName == player.displayName.string
		val text = Text.empty().append(serverInfo.toText())
		if (isSender) text.append("你悄悄对".text.styled { it.withColor(GRAY) })
			.append(riguruMessageConfig.playerNameWrapper.replace("%playerName%", receiver).replace("%displayName%", receiver))
			.append(toText())
		else text.append(sender.toText())
			.append("悄悄对你说".text.styled { it.withColor(GRAY) })
			.append(toText())
		return text
	}

	override fun whisper(message: Message) {
		this.server.playerManager.playerList
			.filter {
				it.entityName == message.receiver || it.displayName.string == message.receiver
				it.entityName == message.sender.name || it.displayName.string == message.sender.displayName
			}
			.forEach {
				it.sendMessage(message)
			}
	}

	override fun broadcast(message: Message) {
		this.server.run {
			sendMessage(message.toFinalBroadcastText())
			playerManager.playerList.forEach {
				it.sendMessage(message)
			}
		}

	}

	private fun ServerPlayerEntity.sendMessage(message: Message) {
		val messageText = if (message.type == CHAT) message.toFinalBroadcastText() else message.toFinalWhisperText(this)
		this.networkHandler.sendPacket(
			ChatMessageS2CPacket(
				messageText,
				Optional.of(messageText),
				(this as (MixinServerPlayerEntity)).getMessageTypeId(MessageType.CHAT),
				MessageSender(message.senderUUID, message.sender.toText()),
				Instant.now(),
				SignatureData.NONE
			)
		)
	}
}