package bakamc.chat.common

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common

 * 文件名 IMessageConfig

 * 创建时间 2022/6/12 11:17

 * @author forpleuvoir

 */
interface IMessageConfig {

	/**
	 * 转发服务器地址
	 */
	val riguruAddress: String

	/**
	 * 服务器信息
	 */
	val serverInfo: ServerInfo

	/**
	 * 服务器名包装器
	 */
	val serverWrapper: String

	/**
	 * 玩家名包装器
	 */
	val playerNameWrapper: String

	/**
	 * 消息包装器
	 */
	val messageWrapper: String

	/**
	 * 聊天顺序 0:服务器 1:玩家名 2:消息
	 */
	val chatOrder: Array<Int>
}