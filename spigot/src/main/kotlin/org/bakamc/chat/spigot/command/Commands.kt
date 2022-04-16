package org.bakamc.chat.spigot.command

import org.bakamc.chat.spigot.BakaChatPlugin
import org.bukkit.Bukkit

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.spigot.command

 * 文件名 Commands

 * 创建时间 2022/4/14 23:05

 * @author forpleuvoir

 */
object Commands {

	fun registerCommand(bakaChatPlugin: BakaChatPlugin) {
		Bukkit.getPluginCommand("message")?.setExecutor(MsgCommand(bakaChatPlugin))
		Bukkit.getPluginCommand("reconnect")?.setExecutor(ReconnectCommand(bakaChatPlugin))
		Bukkit.getPluginCommand("reloadConfig")?.setExecutor(ReloadCommand(bakaChatPlugin))
	}
}