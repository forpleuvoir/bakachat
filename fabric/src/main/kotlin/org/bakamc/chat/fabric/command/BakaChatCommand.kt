package org.bakamc.chat.fabric.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric.command

 * 文件名 BakaChatCommand

 * 创建时间 2022/4/16 23:19

 * @author forpleuvoir

 */
object BakaChatCommand {
	fun init(dispatcher: CommandDispatcher<ServerCommandSource>) {
		MsgCommand.init(dispatcher)
		ReconnectCommand.init(dispatcher)
		ReloadCommand.init(dispatcher)
	}
}