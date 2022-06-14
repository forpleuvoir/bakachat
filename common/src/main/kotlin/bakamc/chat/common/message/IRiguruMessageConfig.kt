package bakamc.chat.common.message

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.message

 * 文件名 IRiguruMessageConfig

 * 创建时间 2022/6/14 23:28

 * @author forpleuvoir

 */
interface IRiguruMessageConfig {

	/**
	 * 聊天顺序 server:服务器 prefix:玩家前缀 playerName:玩家名 3:message
	 */
	val chatSort: Array<String>

	/**
	 * 服务器名包装器
	 *
	 * 占位符
	 *
	 * 服务器名 %serverName%
	 */
	val serverWrapper: String

	/**
	 * 玩家名包装器
	 *
	 * 占位符
	 *
	 * 玩家名 %playerName%
	 *
	 * 玩家显示名 %displayName%
	 */
	val playerNameWrapper: String

	/**
	 * 消息包装器
	 *
	 * 占位符
	 *
	 * 消息内容 %message%
	 */
	val messageWrapper: String
}