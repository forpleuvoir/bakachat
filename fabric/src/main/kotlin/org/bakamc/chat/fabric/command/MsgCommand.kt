package org.bakamc.chat.fabric.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.bakamc.chat.fabric.BakaChat

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric.command

 * 文件名 MsgCommand

 * 创建时间 2022/4/16 23:24

 * @author forpleuvoir

 */
object MsgCommand {

	fun init(dispatcher: CommandDispatcher<ServerCommandSource>) {
		dispatcher.register(
			literal("bakachat:message")
				.then(
					argument("player", StringArgumentType.string())
						.then(
							argument("message", StringArgumentType.string())
								.executes {
									val player = it.source.player
									val target = StringArgumentType.getString(it, "player")
									if (player.entityName == target) {
										it.source.sendError(Text.of("不要自言自语"))
										return@executes 1
									}
									val message = StringArgumentType.getString(it, "message")
									BakaChat.messageHandler.sendMessage(player, message, target)
									1
								}
						)
				)
		)
	}
}