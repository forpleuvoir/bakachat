package org.bakamc.chat.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.MinecraftServer
import org.bakamc.chat.fabric.command.BakaChatCommand
import java.util.function.Consumer

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric

 * 文件名 BakaChat

 * 创建时间 2022/4/16 20:23

 * @author forpleuvoir

 */
object BakaChat : ModInitializer {

	const val MOD_ID = "bakachat"

	lateinit var server: MinecraftServer

	override fun onInitialize() {

		CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			BakaChatCommand.init(dispatcher)
		}
	}


	@JvmStatic
	lateinit var messageHandler: MessageHandler

	@JvmStatic
	fun hasMessageHandler(action: Consumer<MessageHandler>) {
		if (this::messageHandler.isInitialized && messageHandler.isOpen) {
			action.accept(messageHandler)
		}
	}

	@JvmStatic
	fun resetMessageHandler(messageHandler: MessageHandler) {
		if (this::messageHandler.isInitialized && BakaChat.messageHandler.isOpen)
			this.messageHandler.close()
		this.messageHandler = messageHandler
		this.messageHandler.connect()
	}
}