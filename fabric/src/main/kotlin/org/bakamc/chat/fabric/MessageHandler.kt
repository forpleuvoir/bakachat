package org.bakamc.chat.fabric

import bakamc.chat.common.*
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
import net.minecraft.util.Formatting.ITALIC
import org.bakamc.chat.fabric.mixin.MixinServerPlayerEntity
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
		Pattern.compile(regex).matcher(message).run {
			while (this.find()) {
				texts.add(Text.Serializer.fromJson(this.group())!!)
			}
		}
		val message: MutableText = Text.empty()
		this.message.split(regex.toRegex()).run {
			forEachIndexed { index, str ->
				message.append(str).let {
					if (index != this.size - 1) it.append(texts[index])
				}
			}
		}
		return message
	}

	override fun ServerInfo.toText(): Text {
		val hoverText = MutableText.of(LiteralTextContent("$serverName\n"))
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
		val hoverText = MutableText.of(LiteralTextContent("§b$name\n"))
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

	override fun Message.toFinalText(): Text {
		TODO("Not yet implemented")
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
			sendMessage(message.toFinalText())
			playerManager.playerList.forEach {
				it.sendMessage(message)
			}
		}

	}

	private fun ServerPlayerEntity.sendMessage(message: Message) {
		val messageText = message.toFinalText()
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