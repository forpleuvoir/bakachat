package org.bakamc.chat.spigot.command

import org.bakamc.chat.spigot.BakaChatPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot.command

 * 文件名 MsgCommand

 * 创建时间 2022/4/14 23:05

 * @author forpleuvoir

 */
class MsgCommand(private val bakaChatPlugin: BakaChatPlugin) : CommandExecutor {

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
		if (args.size != 2) return false
		if (sender is Player && sender.isOnline) {
			if (sender.displayName == args[0]) {
				sender.sendMessage("§c不要自言自语")
				return true
			}
			bakaChatPlugin.messageHandler.sendMessage(sender, args[1], args[0])
			return true
		}
		return false
	}

}