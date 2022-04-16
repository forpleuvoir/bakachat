package org.bakamc.chat.fabric

import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import org.bakamc.chat.common.message.IMessageConfig
import org.bakamc.chat.common.message.textevent.ClickEvent
import org.bakamc.chat.common.message.textevent.HoverEvent
import org.bakamc.chat.common.message.textevent.HoverEvent.*
import org.bakamc.chat.common.message.textevent.HoverEvent.Action.*
import org.bakamc.chat.common.util.FileUtil
import org.bakamc.chat.common.util.JsonUtil.gson
import org.bakamc.chat.common.util.JsonUtil.ifNullOr
import org.bakamc.chat.common.util.replace
import org.bakamc.chat.fabric.config.DefaultConfig
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric

 * 文件名 MessageConfig

 * 创建时间 2022/4/16 20:35

 * @author forpleuvoir

 */
class MessageConfig : IMessageConfig {

	private val config: JsonObject

	init {
		val dir = File(FabricLoader.getInstance().configDir.toString() + "/BakaChat")
		if (!dir.exists()) dir.mkdir()
		val configFile = File(dir, "config.yml")
		if (!configFile.exists()) {
			configFile.createNewFile()
			FileUtil.writeFile(configFile, DefaultConfig.DEFAULT)
		}
		val configStr = FileUtil.readFile(configFile)
		config = gson.toJsonTree(Yaml().loadAs(configStr, HashMap::class.java)) as JsonObject
	}

	override fun message_exp(message: String): String {
		return config.ifNullOr("messageExp", ": $message").replace("%message%", message)
	}

	override fun sender_name_exp(sender_name: String): String {
		return config.ifNullOr("senderNameExp", ": $sender_name").replace("%sender_name%", sender_name)
	}

	override fun sender_name_hover_evnet(sender_name: String, sender_uuid: String): HoverEvent? {
		try {
			val map = mapOf(
				"%sender_name%" to sender_name,
				"%sender_uuid%" to sender_uuid
			)
			config.getHoverEvent("senderNameHoverEvent", map)?.let {
				if (it is EntityContent) {
					if (it.id == "90bd256d-49e3-4423-8697-3c634eed9ae5") {
						return HoverEvent(EntityContent("minecraft:pig", it.id, "猪比 ${it.name}"))
					} else if (it.id == "83f92c7b-ecf9-4220-b417-8febe789cdc7") {
						return HoverEvent(EntityContent("minecraft:pig", it.id, "憨批 ${it.name}"))
					}
				}
				return HoverEvent(it)
			}
		} catch (e: Exception) {
			println("配置加载失败")
			e.printStackTrace()
			return null
		}
		return HoverEvent(EntityContent("minecraft:player", sender_uuid, sender_name))
	}

	override fun sender_name_click_evnet(sender_name: String, sender_uuid: String): ClickEvent? {
		try {
			val map = mapOf(
				"%sender_name%" to sender_name,
				"%sender_uuid%" to sender_uuid
			)
			config.getClickEvent("senderNameClickEvent", map)?.let { return it }
		} catch (e: Exception) {
			println("配置加载失败")
			e.printStackTrace()
			return null
		}
		return ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bakacaht:message $sender_name")
	}

	override val sender_server: String
		get() = config.ifNullOr("server", "§6[§d养老服§6]§r")

	override fun server_name_hover_event(server_name: String): HoverEvent? {
		try {
			config.getHoverEvent("serverNameHoverEvent", mapOf("%server_name%" to server_name))?.let { return HoverEvent(it) }
		} catch (e: Exception) {
			println("配置加载失败")
			e.printStackTrace()
			return null
		}
		return HoverEvent(HoverEvent.TextContent(listOf(server_name, " 点击传送至此服务器")))
	}

	override fun server_name_click_event(server_name: String): ClickEvent? {
		try {
			config.getClickEvent("serverNameClickEvent", mapOf("%server_name%" to server_name))?.let { return it }
		} catch (e: Exception) {
			println("配置加载失败")
			e.printStackTrace()
			return null
		}
		return ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/server $server_name")
	}

	override val riguru_address: String
		get() = config.ifNullOr("riguruAddress", "ws://127.0.0.1:3499")

	private fun JsonObject.getHoverEvent(path: String, map: Map<String, String>): HoverEvent.Content? {
		this[path]?.let {
			it as JsonObject
			val action = HoverEvent.Action.valueOf(it.ifNullOr("action", ""))
			val content = it["content"].asJsonObject
			val eventContent = when (action) {
				SHOW_TEXT   -> TextContent(ArrayList<String>().apply {
					content.getAsJsonArray("texts").forEach { e -> this.add(e.asString.replace(map)) }
				})
				SHOW_ITEM   ->
					ItemContent(content.get("id").asString!!.replace(map),
					            content.get("count").asInt,
					            content.get("tag").asString!!.replace(map))
				SHOW_ENTITY ->
					EntityContent(
						content.get("type").asString!!.replace(map),
						content.get("id").asString!!.replace(map),
						content.get("name").asString!!.replace(map)
					)
			}
			return eventContent
		}
		return null
	}

	private fun JsonObject.getClickEvent(path: String, map: Map<String, String>): ClickEvent? {
		this[path]?.let {
			it as JsonObject
			val action = org.bakamc.chat.common.message.textevent.ClickEvent.Action.valueOf(it.get("action").asString!!)
			val value = it.get("value").asString!!.replace(map)
			return ClickEvent(action, value)
		}
		return null
	}
}