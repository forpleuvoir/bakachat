package org.bakamc.chat.fabric.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.bakamc.chat.fabric.BakaChat

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric.command

 * 文件名 ReloadCommand

 * 创建时间 2022/4/16 23:35

 * @author forpleuvoir

 */
object ReloadCommand {
	fun init(dispatcher: CommandDispatcher<ServerCommandSource>) {
		dispatcher.register(
			literal("bakachat:reloadConfig")
				.executes {
					BakaChat.messageHandler.reload()
					it.source.sendFeedback(Text.of("已重新加载配置"), false)
					1
				}
		)
	}
}