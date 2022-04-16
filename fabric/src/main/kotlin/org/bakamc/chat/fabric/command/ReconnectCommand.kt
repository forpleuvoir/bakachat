package org.bakamc.chat.fabric.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.bakamc.chat.fabric.BakaChat
import org.bakamc.chat.fabric.MessageHandler

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric.command

 * 文件名 ReconnectCommand

 * 创建时间 2022/4/16 23:31

 * @author forpleuvoir

 */
object ReconnectCommand {

	fun init(dispatcher: CommandDispatcher<ServerCommandSource>) {
		dispatcher.register(
			literal("bakachat:reconnect")
				.executes {
					BakaChat.messageHandler.reload()
					BakaChat.messageHandler.close()
					BakaChat.messageHandler = MessageHandler(BakaChat.server)
					BakaChat.messageHandler.connect()
					it.source.sendFeedback(Text.of("已重新加连接服务"), false)
					1
				}
		)
	}

}