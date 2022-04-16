package org.bakamc.chat.spigot

import org.bakamc.chat.spigot.command.Commands
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot

 * 文件名 BakaChatPlugin

 * 创建时间 2022/4/10 13:00

 * @author forpleuvoir

 */
class BakaChatPlugin : JavaPlugin(), Listener {

	lateinit var messageHandler: MessageHandler

	override fun onEnable() {
		Commands.registerCommand(this)
		messageHandler = MessageHandler(this)
		server.pluginManager.registerEvents(this, this)
		messageHandler.connect()
	}

	@EventHandler
	fun asyncPlayerChatEvent(event: AsyncPlayerChatEvent) {
		if (event.isCancelled) return
		if (this::messageHandler.isInitialized && messageHandler.isOpen)
			messageHandler.sendMessage(event.player, event.message)
		event.isCancelled = true
	}

	override fun onDisable() {
		super.onDisable()
		if (this::messageHandler.isInitialized && messageHandler.isOpen) {
			messageHandler.close()
		}
	}


}