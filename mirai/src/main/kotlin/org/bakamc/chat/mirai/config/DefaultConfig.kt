package org.bakamc.chat.mirai.config

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.mirai.config

 * 文件名 DefaultConfig

 * 创建时间 2022/4/16 23:49

 * @author forpleuvoir

 */
object DefaultConfig {

	val DEFAULT: String = """
riguruAddress: "ws://127.0.0.1:3499"
groups: [253779435]
bot_id: 2481735118
messageExp: ": %message%"
senderNameExp: "§6[§b%sender_name%§6]§r"
server: "§6[§9群聊§6]§r"
senderNameHoverEvent:
  action: "SHOW_ENTITY"
  content:
    type: "minecraft:player"
    id: "%sender_uuid%"
    name: "%sender_name%"
senderNameClickEvent:
  action: "SUGGEST_COMMAND"
  value: ""
serverNameHoverEvent:
  action: "SHOW_TEXT"
  content:
    texts:
      - "%server_name%"
serverNameClickEvent:
  action: "SUGGEST_COMMAND"
  value: ""
""".trimStart()
}