package bakamc.chat.common.message

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.message

 * 文件名 ServerInfo

 * 创建时间 2022/6/4 17:48

 * @author forpleuvoir

 */
class ServerInfo(
	/**
	 * 服务器ID
	 */
	val serverId: String,
	/**
	 * 服务器名称
	 */
	val serverName: String,
	/**
	 * 服务器描述
	 */
	val description: String,
) {
	override fun toString(): String {
		return "ServerInfo(serverId='$serverId', serverName='$serverName', description='$description')"
	}
}