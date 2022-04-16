package org.bakamc.chat.spigot.command

import org.bakamc.chat.spigot.BakaChatPlugin
import org.bakamc.chat.spigot.MessageHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot.command

 * 文件名 ReconnectCommand

 * 创建时间 2022/4/16 14:33

 * @author forpleuvoir

 */
class ReconnectCommand(private val bakaChatPlugin: BakaChatPlugin) : CommandExecutor {
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		bakaChatPlugin.messageHandler.reload()
		bakaChatPlugin.messageHandler.close()
		bakaChatPlugin.messageHandler = MessageHandler(bakaChatPlugin)
		bakaChatPlugin.messageHandler.connect()
		sender.sendMessage("已重新连接服务")
		return true
	}

}