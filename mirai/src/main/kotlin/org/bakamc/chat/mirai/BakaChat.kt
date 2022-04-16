package org.bakamc.chat.mirai

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.mirai

 * 文件名 BakaChat

 * 创建时间 2022/4/17 2:15

 * @author forpleuvoir

 */
object BakaChat : KotlinPlugin(
	JvmPluginDescription(
		id = "org.bakamc.chat",
		version = "0.1.0"
	) {
		name("BakaChat")
	}
) {
	lateinit var messageHandler: MessageHandler

	override fun onEnable() {
		messageHandler = MessageHandler(this)
		messageHandler.connect()
		this.globalEventChannel().subscribe<GroupMessageEvent> {
			if (messageHandler.config.groups.contains(it.group.id)) {
				messageHandler.sendMessage(it.sender, it.message.contentToString())
			}
			ListeningStatus.LISTENING
		}

	}

	override fun onDisable() {
		messageHandler.close()
	}
}