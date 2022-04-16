package org.bakamc.chat.fabric.config

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric.config

 * 文件名 DefaultConfig

 * 创建时间 2022/4/16 23:49

 * @author forpleuvoir

 */
object DefaultConfig {

	val DEFAULT: String = """
riguruAddress: "ws://127.0.0.1:3499"
messageExp: ": %message%"
senderNameExp: "§6[§b%sender_name%§6]§r"
server: "§6[§d养老服§6]§r"
senderNameHoverEvent:
  action: "SHOW_ENTITY"
  content:
    type: "minecraft:player"
    id: "%sender_uuid%"
    name: "%sender_name%"
senderNameClickEvent:
  action: "SUGGEST_COMMAND"
  value: "/bakachat:message %sender_name%"
serverNameHoverEvent:
  action: "SHOW_TEXT"
  content:
    texts:
      - "%server_name%\n"
      - "点击传送至该服务器"
serverNameClickEvent:
  action: "SUGGEST_COMMAND"
  value: "/server mod"
""".trimStart()
}