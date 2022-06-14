package bakamc.chat.common.message

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.message

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


}