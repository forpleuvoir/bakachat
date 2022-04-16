package org.bakamc.chat.spigot

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.*
import net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ENTITY
import net.md_5.bungee.api.chat.hover.content.Entity
import net.md_5.bungee.api.chat.hover.content.Item
import net.md_5.bungee.api.chat.hover.content.Text
import org.bakamc.chat.common.message.AbstractMessageHandler
import org.bakamc.chat.common.message.Message
import org.bakamc.chat.common.message.MessageType
import org.bakamc.chat.common.message.MessageType.CHAT
import org.bakamc.chat.common.message.MessageType.WHISPER
import org.bakamc.chat.common.message.textevent.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.regex.Pattern

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot

 * 文件名 MessageHandler

 * 创建时间 2022/4/12 23:22

 * @author forpleuvoir

 */
class MessageHandler(private val bakaChatPlugin: BakaChatPlugin) : AbstractMessageHandler<Player>(MessageConfig(bakaChatPlugin)) {

	override fun sendMessage(player: Player, message: String, target: String?, targetId: String?) {
		val senderName: String
		val senderUUID: String
		val senderServer: String = messageConfig.sender_server
		player.let {
			senderName = it.displayName
			senderUUID = it.uniqueId.toString()
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
						val stack: ItemStack? = if (num == "i") player.inventory.itemInMainHand else {
							val index = num.toInt() - 1
							player.inventory.getItem(index)
						}
						if (stack?.type?.isAir == false) {
							val itemStack = CraftItemStack.asNMSCopy(stack)
							val item = itemStack.c()
							val translationKey = item.a()
							val nbtTagCompound = itemStack.s()?.g()?.e_() ?: ""
							val key = stack.type.key
							val rarity = itemStack.z().e.A
							strBuilder.append("§{$translationKey}")
							messageEvents.add(
								HoverEvent(
									HoverEvent.ItemContent(
										key.toString(),
										stack.amount,
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
				message = msg,
				messageHoverEvents = messageEvents
			)
		)
	}

	override fun broadcast(message: Message) {
		Bukkit.getServer().onlinePlayers.forEach {
			it.sendMessage(message)
		}
	}

	override fun whisper(message: Message) {
		Bukkit.getServer().onlinePlayers
			.filter { it.displayName == message.receiver_name || it.uniqueId == message.receiverUUID }
			.forEach { it.sendMessage(message) }
		Bukkit.getServer().onlinePlayers
			.filter { it.displayName == message.sender_name || it.uniqueId == message.senderUUID }
			.forEach { it.sendMessage(message) }
	}

	override fun reload() {
		bakaChatPlugin.reloadConfig()
		messageConfig = MessageConfig(bakaChatPlugin)
	}

	private fun Player.sendMessage(message: Message) {
		val serverName = TextComponent(message.server_name).apply {
			message.server_hover_event?.let { event ->
				this.hoverEvent = when (event.action) {
					HoverEvent.Action.SHOW_ENTITY -> {
						val entity = event.content as HoverEvent.EntityContent
						HoverEvent(
							net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ENTITY, Entity(
								entity.type, entity.id, TextComponent(entity.name)
							)
						)
					}
					HoverEvent.Action.SHOW_ITEM   -> {
						val item = event.content as HoverEvent.ItemContent
						HoverEvent(
							net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ITEM, Item(
								item.id, item.count, ItemTag.ofNbt(item.tag)
							)
						)
					}
					HoverEvent.Action.SHOW_TEXT   -> {
						val text = event.content as HoverEvent.TextContent
						val texts = ArrayList<Text>().apply { text.texts.forEach { str -> this.add(Text(str)) } }
						HoverEvent(
							net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
							*texts.toTypedArray()
						)
					}
				}
			}
			message.server_click_event?.let { event ->
				this.clickEvent = ClickEvent(ClickEvent.Action.valueOf(event.action.toString()), event.value)
			}
		}
		val playerName = TextComponent(message.sender_name).apply {
			message.sender_hover_event?.let { event ->
				this.hoverEvent = when (event.action) {
					HoverEvent.Action.SHOW_ENTITY -> {
						val entity = event.content as HoverEvent.EntityContent
						HoverEvent(
							net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ENTITY, Entity(
								entity.type, entity.id, TextComponent(entity.name)
							)
						)
					}
					HoverEvent.Action.SHOW_ITEM   -> {
						val item = event.content as HoverEvent.ItemContent
						HoverEvent(
							net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ITEM, Item(
								item.id, item.count, ItemTag.ofNbt(item.tag)
							)
						)
					}
					HoverEvent.Action.SHOW_TEXT   -> {
						val text = event.content as HoverEvent.TextContent
						val texts = ArrayList<TextComponent>().apply { text.texts.forEach { str -> this.add(TextComponent(str)) } }
						HoverEvent(
							net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, Text(texts.toTypedArray())
						)
					}
				}
			}
			message.sender_click_event?.let { event ->
				this.clickEvent = ClickEvent(ClickEvent.Action.valueOf(event.action.toString()), event.value)
			}
		}
		val atList = ArrayList<String>()
		val messageStr = handlerAt(message.message, atList)
		val msg = ArrayList<BaseComponent>().apply {
			if (message.type == CHAT && atList.contains(this@sendMessage.displayName))
				world.playSound(this@sendMessage.location, Sound.ENTITY_CREEPER_PRIMED, 1.5f, 1f)
			val regex = "(?<=§\\{)[^}]+"
			val matches: ArrayList<String> = ArrayList()
			val pattern = Pattern.compile(regex)
			val matcher = pattern.matcher(messageStr)
			while (matcher.find()) {
				matches.add(matcher.group())
			}
			messageStr.split(Regex(regex)).forEachIndexed { index, str ->
				this.add(TextComponent(str.removePrefix("}")))
				if (index != messageStr.split(Regex(regex)).size - 1) {
					val hoverEvent = message.messageHoverEvents[index]
					if (hoverEvent.action == HoverEvent.Action.SHOW_ITEM) {
						val item = hoverEvent.content as HoverEvent.ItemContent
						val text = TextComponent("[").apply {
							addExtra(TranslatableComponent(matches[index]))
							val countStr = if (item.count < 2) "" else " x${item.count}"
							addExtra(TextComponent("$countStr]§r"))
							this.hoverEvent = HoverEvent(
								net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_ITEM,
								Item(item.id, item.count, ItemTag.ofNbt(item.tag))
							)
							this.color = ChatColor.getByChar(item.rarity)
						}
						this.add(text)
					}
				}
			}
		}
		when (message.type) {
			CHAT    -> spigot().sendMessage(message.senderUUID, serverName, playerName, *msg.toTypedArray())
			WHISPER -> {
				val receiverName = TextComponent(messageConfig.sender_name_exp(message.receiver_name)).apply {
					this.hoverEvent = HoverEvent(SHOW_ENTITY, Entity(
						"minecraft:player",
						message.receiverUUID.toString(),
						TextComponent(message.receiver_name)
					))
				}
				if (message.sender_name == messageConfig.sender_name_exp(this.displayName)) {
					spigot().sendMessage(
						message.senderUUID,
						serverName,
						TextComponent("你悄悄对").apply {
							addExtra(receiverName)
							addExtra("说")
							this.color = ChatColor.GRAY
						},
						*msg.toTypedArray()
					)
				} else {
					world.playSound(this.location, Sound.BLOCK_ANVIL_PLACE, 1.5f, 1f)
					spigot().sendMessage(
						message.senderUUID,
						serverName,
						playerName.apply {
							addExtra(TextComponent("悄悄对你说"))
							this.color = ChatColor.GRAY
						},
						*msg.toTypedArray()
					)
				}
			}
		}
	}


}