package org.bakamc.chat.common.message

import org.bakamc.chat.common.message.textevent.ClickEvent
import org.bakamc.chat.common.message.textevent.HoverEvent

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.message

 * 文件名 MessageConfig

 * 创建时间 2022/4/10 18:53

 * @author forpleuvoir

 */
interface IMessageConfig {


	fun message_exp(message: String): String

	fun sender_name_exp(sender_name: String): String

	fun sender_name_hover_evnet(sender_name: String, sender_uuid: String): HoverEvent?

	fun sender_name_click_evnet(sender_name: String, sender_uuid: String): ClickEvent?

	val sender_server: String

	fun server_name_hover_event(server_name: String): HoverEvent?

	fun server_name_click_event(server_name: String): ClickEvent?

	val riguru_address: String

}