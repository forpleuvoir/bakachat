package org.bakamc.chat.spigot

import org.bakamc.chat.common.message.IMessageConfig
import org.bakamc.chat.common.message.textevent.ClickEvent
import org.bakamc.chat.common.message.textevent.HoverEvent
import org.bakamc.chat.common.util.replace
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot

 * 文件名 MessageConfig

 * 创建时间 2022/4/13 22:11

 * @author forpleuvoir

 */
class MessageConfig(val plugin: JavaPlugin) : IMessageConfig {

	init {
		plugin.saveDefaultConfig()
	}

	private val config = plugin.config

	override fun message_exp(message: String): String {
		return config.getString("messageExp")?.replace("%message%", message) ?: " : $message"
	}

	override fun sender_name_exp(sender_name: String): String {
		return config.getString("senderNameExp")?.replace("%sender_name%", sender_name) ?: "[${sender_name}]"
	}

	override fun sender_name_hover_evnet(sender_name: String, sender_uuid: String): HoverEvent? {
		try {
			val map = mapOf(
				"%sender_name%" to sender_name,
				"%sender_uuid%" to sender_uuid
			)
			config.getHoverEvent("senderNameHoverEvent", map)?.let {
				if (it is HoverEvent.EntityContent) {
					if (it.id == "90bd256d-49e3-4423-8697-3c634eed9ae5") {
						return HoverEvent(HoverEvent.EntityContent("minecraft:pig", it.id, "猪比 ${it.name}"))
					} else if (it.id == "83f92c7b-ecf9-4220-b417-8febe789cdc7") {
						return HoverEvent(HoverEvent.EntityContent("minecraft:pig", it.id, "憨批 ${it.name}"))
					}
				}
				return HoverEvent(it)
			}
		} catch (e: Exception) {
			plugin.logger.info("配置加载失败")
			plugin.logger.info(e.message)
			return null
		}
		return HoverEvent(HoverEvent.EntityContent("minecraft:player", sender_uuid, sender_name))
	}

	override fun sender_name_click_evnet(sender_name: String, sender_uuid: String): ClickEvent? {
		try {
			val map = mapOf(
				"%sender_name%" to sender_name,
				"%sender_uuid%" to sender_uuid
			)
			config.getClickEvent("senderNameClickEvent", map)?.let { return it }
		} catch (e: Exception) {
			plugin.logger.info("配置加载失败")
			plugin.logger.info(e.message)
			return null
		}
		return ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bakacaht:message $sender_name")
	}

	override val sender_server: String
		get() = config.getString("server") ?: "§e[测试服]"

	override fun server_name_hover_event(server_name: String): HoverEvent? {
		try {
			config.getHoverEvent("serverNameHoverEvent", mapOf("%server_name%" to server_name))?.let { return HoverEvent(it) }
		} catch (e: Exception) {
			plugin.logger.info("配置加载失败")
			plugin.logger.info(e.message)
			return null
		}
		return HoverEvent(HoverEvent.TextContent(listOf(server_name, " 点击传送至此服务器")))
	}

	override fun server_name_click_event(server_name: String): ClickEvent? {
		try {
			config.getClickEvent("serverNameClickEvent", mapOf("%server_name%" to server_name))?.let { return it }
		} catch (e: Exception) {
			plugin.logger.info("配置加载失败")
			plugin.logger.info(e.message)
			return null
		}
		return ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/server $server_name")
	}

	override val riguru_address: String
		get() = config.getString("riguruAddress") ?: "ws://127.0.0.1:3499"

	private fun FileConfiguration.getHoverEvent(path: String, map: Map<String, String>): HoverEvent.Content? {
		this.getConfigurationSection(path)?.let {
			val action = HoverEvent.Action.valueOf(it.getString("action")!!)
			val content = it.getConfigurationSection("content")!!
			val eventContent = when (action) {
				HoverEvent.Action.SHOW_TEXT   ->
					HoverEvent.TextContent(content.getStringList("texts").apply { replaceAll { str -> str.replace(map) } })
				HoverEvent.Action.SHOW_ITEM   ->
					HoverEvent.ItemContent(content.getString("id")!!.replace(map),
					                       content.getInt("count"),
					                       content.getString("tag")!!.replace(map))
				HoverEvent.Action.SHOW_ENTITY ->
					HoverEvent.EntityContent(
						content.getString("type")!!.replace(map),
						content.getString("id")!!.replace(map),
						content.getString("name")!!.replace(map)
					)
			}
			return eventContent
		}
		return null
	}

	private fun FileConfiguration.getClickEvent(path: String, map: Map<String, String>): ClickEvent? {
		this.getConfigurationSection(path)?.let {
			val action = ClickEvent.Action.valueOf(it.getString("action")!!)
			val value = it.getString("value")!!.replace(map)
			return ClickEvent(action, value)
		}
		return null
	}

}