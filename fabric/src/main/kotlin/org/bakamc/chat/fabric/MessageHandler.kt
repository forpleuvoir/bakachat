package org.bakamc.chat.fabric

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundCategory.BLOCKS
import net.minecraft.sound.SoundEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent.Action
import net.minecraft.text.HoverEvent.EntityContent
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.bakamc.chat.common.message.AbstractMessageHandler
import org.bakamc.chat.common.message.Message
import org.bakamc.chat.common.message.MessageType.CHAT
import org.bakamc.chat.common.message.MessageType.WHISPER
import org.bakamc.chat.common.message.textevent.HoverEvent
import org.bakamc.chat.common.message.textevent.HoverEvent.ItemContent
import org.bakamc.chat.common.util.JsonUtil.gson
import org.bakamc.chat.fabric.mixin.MixinItemStackContent
import java.util.regex.Pattern

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric

 * 文件名 MessageHandler

 * 创建时间 2022/4/16 20:34

 * @author forpleuvoir

 */
class MessageHandler(private val server: MinecraftServer) : AbstractMessageHandler<PlayerEntity>(MessageConfig()) {

	override fun reload() {
		this.messageConfig = MessageConfig()
	}

	override fun sendMessage(player: PlayerEntity, message: String, target: String?, targetId: String?) {
		val senderName: String
		val senderUUID: String
		val senderServer: String = messageConfig.sender_server
		player.let {
			senderName = it.entityName
			senderUUID = it.uuidAsString
		}
		val messageEvents = ArrayList<HoverEvent>()
		val regex = "(%\\d)|(%i)"
		val pattern = Pattern.compile(regex)
		val matcher = pattern.matcher(message)
		val matches: ArrayList<String> = ArrayList()
		val msg: String = message.run {
			val strBuilder = StringBuilder()
			while (matcher.find()) {
				matches.add(matcher.group())
			}
			if (matches.size > 0) {
				val s: List<String> = this.split(regex.toRegex())
				s.forEachIndexed { i, str ->
					strBuilder.append(str)
					if (i != (s.size - 1)) {
						val num = matches[i][1].toString()
						val stack: ItemStack? = if (num == "i") player.inventory.mainHandStack else {
							val index = num.toInt() - 1
							player.inventory.getStack(index)
						}
						if (stack != null && stack.item != Items.AIR) {
							val item = stack.item
							val translationKey = item.translationKey
							val nbtTagCompound = stack.nbt?.asString() ?: ""
							val key = Registry.ITEM.getId(item)
							val rarity = stack.rarity.formatting.code
							strBuilder.append("§{$translationKey}")
							messageEvents.add(
								HoverEvent(
									ItemContent(
										key.toString(),
										stack.count,
										nbtTagCompound,
										rarity
									)
								)
							)
						} else {
							strBuilder.append(matches[i])
						}
					}
				}
			} else {
				strBuilder.append(this)
			}
			strBuilder.toString()
		}
		sendMessage(
			Message(
				type = if (target != null || targetId != null) WHISPER else CHAT,
				receiver_name = target ?: "",
				receiver_uuid = targetId ?: "",
				sender_name = senderName,
				sender_hover_event = messageConfig.sender_name_hover_evnet(senderName, senderUUID),
				sender_click_event = messageConfig.sender_name_click_evnet(senderName, senderUUID),
				sender_uuid = senderUUID,
				server_name = senderServer,
				server_hover_event = messageConfig.server_name_hover_event(senderServer),
				server_click_event = messageConfig.server_name_click_event(senderServer),
				message = msg,
				messageHoverEvents = messageEvents
			)
		)
	}

	override fun broadcast(message: Message) {
		this.server.sendSystemMessage(LiteralText(message.sender_name).append(Text.of(message.message)), message.senderUUID)
		server.playerManager.playerList.forEach {
			it.sendMessage(message)
		}
	}

	override fun whisper(message: Message) {
		val players = server.playerManager.playerList
		players
			.filter { it.entityName.toString() == message.receiver_name || it.uuid == message.receiverUUID }
			.forEach { it.sendMessage(message) }
		players
			.filter { it.entityName.toString() == message.sender_name || it.uuid == message.senderUUID }
			.forEach { it.sendMessage(message) }
	}

	private fun ServerPlayerEntity.sendMessage(message: Message) {
		val world = this.entityWorld
		val serverName = LiteralText(message.server_name).apply {
			var hoverEvent: net.minecraft.text.HoverEvent? = null
			var clickEvent: ClickEvent? = null
			message.server_hover_event?.let { event ->
				hoverEvent = when (event.action) {
					HoverEvent.Action.SHOW_ENTITY -> {
						val entity = event.content as HoverEvent.EntityContent
						net.minecraft.text.HoverEvent(
							Action.SHOW_ENTITY,
							EntityContent.parse(gson.toJsonTree(entity))
						)
					}
					HoverEvent.Action.SHOW_ITEM   -> {
						val item = event.content as ItemContent
						net.minecraft.text.HoverEvent(
							Action.SHOW_ITEM,
							MixinItemStackContent.parseItemStackContent(gson.toJsonTree(item))
						)
					}
					HoverEvent.Action.SHOW_TEXT   -> {
						val text = event.content as HoverEvent.TextContent
						val texts = LiteralText("").apply { text.texts.forEach(this::append) }
						net.minecraft.text.HoverEvent(Action.SHOW_TEXT, texts)
					}
				}
			}
			message.server_click_event?.let { event ->
				clickEvent = ClickEvent(ClickEvent.Action.valueOf(event.action.toString()), event.value)
			}
			styled { it.withClickEvent(clickEvent).withHoverEvent(hoverEvent) }
		}
		val playerName = LiteralText(message.sender_name).apply {
			var hoverEvent: net.minecraft.text.HoverEvent? = null
			var clickEvent: ClickEvent? = null
			message.sender_hover_event?.let { event ->
				hoverEvent = when (event.action) {
					HoverEvent.Action.SHOW_ENTITY -> {
						val entity = event.content as HoverEvent.EntityContent
						net.minecraft.text.HoverEvent(
							Action.SHOW_ENTITY,
							EntityContent.parse(gson.toJsonTree(entity))
						)
					}
					HoverEvent.Action.SHOW_ITEM   -> {
						val item = event.content as ItemContent
						net.minecraft.text.HoverEvent(
							Action.SHOW_ITEM,
							MixinItemStackContent.parseItemStackContent(gson.toJsonTree(item))
						)
					}
					HoverEvent.Action.SHOW_TEXT   -> {
						val text = event.content as HoverEvent.TextContent
						val texts = LiteralText("").apply { text.texts.forEach(this::append) }
						net.minecraft.text.HoverEvent(Action.SHOW_TEXT, texts)
					}
				}
			}
			message.sender_click_event?.let { event ->
				clickEvent = ClickEvent(ClickEvent.Action.valueOf(event.action.toString()), event.value)
			}
			styled { it.withClickEvent(clickEvent).withHoverEvent(hoverEvent) }
		}
		val atList = ArrayList<String>()
		val messageStr = handlerAt(message.message, atList)
		val msg = LiteralText("").apply {
			if (message.type == CHAT && atList.contains(this@sendMessage.entityName))
				world.playSound(this@sendMessage,
				                this@sendMessage.blockPos,
				                SoundEvents.ENTITY_CREEPER_PRIMED,
				                SoundCategory.HOSTILE,
				                10f,
				                1.0f
				)
			val regex = "(?<=§\\{)[^}]+"
			val matches: ArrayList<String> = ArrayList()
			val pattern = Pattern.compile(regex)
			val matcher = pattern.matcher(messageStr)
			while (matcher.find()) {
				matches.add(matcher.group())
			}
			messageStr.split(Regex(regex)).forEachIndexed { index, str ->
				this.append(LiteralText(str.removePrefix("}")))
				if (index != messageStr.split(Regex(regex)).size - 1) {
					val hoverEvent = message.messageHoverEvents[index]
					if (hoverEvent.action == HoverEvent.Action.SHOW_ITEM) {
						val item = hoverEvent.content as ItemContent
						val text = LiteralText("[").apply {
							append(TranslatableText(matches[index]))
							val countStr = if (item.count < 2) "" else " x${item.count}"
							append(LiteralText("$countStr]§r"))
							styled {
								it.withHoverEvent(
									net.minecraft.text.HoverEvent(
										Action.SHOW_ITEM,
										MixinItemStackContent.parseItemStackContent(gson.toJsonTree(item)))
								).withColor(Formatting.byCode(item.rarity))
							}
						}
						this.append(text)
					}
				}
			}
		}
		when (message.type) {
			CHAT    -> this.sendMessage(LiteralText("").append(serverName).append(playerName).append(msg),
			                            net.minecraft.network.MessageType.CHAT,
			                            message.senderUUID)
			WHISPER -> {
				val receiverName = LiteralText(messageConfig.sender_name_exp(message.receiver_name)).styled {
					it.withHoverEvent(
						net.minecraft.text.HoverEvent(
							Action.SHOW_ENTITY,
							EntityContent(Registry.ENTITY_TYPE.get(Identifier("minecraft:player")),
							              message.receiverUUID,
							              Text.of(message.receiver_name)
							)
						)
					)
				}
				if (message.sender_name == messageConfig.sender_name_exp(this.entityName)) {
					this.sendMessage(
						LiteralText("")
							.append(serverName)
							.append(LiteralText("你悄悄对"))
							.append(receiverName).styled { it.withColor(Formatting.GRAY) }
							.append("说")
							.append(msg),
						net.minecraft.network.MessageType.CHAT,
						message.senderUUID,
					)
				} else {
					world.playSound(this, this.blockPos, SoundEvents.BLOCK_ANVIL_PLACE, BLOCKS, 10f, 1f)
					this.sendMessage(
						LiteralText("")
							.append(serverName)
							.append(playerName)
							.append(LiteralText("悄悄对你说").styled { it.withColor(Formatting.GRAY) })
							.append(msg),
						net.minecraft.network.MessageType.CHAT,
						message.senderUUID,
					)
				}
			}
		}
	}
}