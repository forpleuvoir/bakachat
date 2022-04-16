package org.bakamc.chat.spigot.command

import org.bakamc.chat.spigot.BakaChatPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot.command

 * 文件名 ReloadCommand

 * 创建时间 2022/4/16 14:09

 * @author forpleuvoir

 */
class ReloadCommand(private val bakaChatPlugin: BakaChatPlugin) : CommandExecutor {
	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		bakaChatPlugin.messageHandler.reload()
		sender.sendMessage("已重新加载配置")
		return true
	}

}