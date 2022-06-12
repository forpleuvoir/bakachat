package bakamc.chat.common

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common

 * 文件名 IMessageHandler

 * 创建时间 2022/6/5 14:23

 * @author forpleuvoir

 */
interface IMessageHandler<T, P> {

	/**
	 * 连接转发服务器
	 */
	fun connect()

	/**
	 * 重连转发服务器
	 */
	fun reconnect()

	/**
	 * 关闭连接
	 */
	fun close()

	/**
	 * 是否已连接服务器
	 */
	val isOpen: Boolean

	/**
	 * 重新加载配置文件
	 */
	fun reload()

	/**
	 * 发送消息
	 * @param player P
	 * @param message T
	 */
	fun sendMessage(player: P, message: String)

	/**
	 * 向服务器发送消息
	 */
	fun sendMessageToRiguru(message: Message)

	/**
	 * 处理接收到的消息
	 *
	 * 在服务器内广播,或者发给某个人
	 *
	 * @param message Message
	 */
	fun receivesMessage(message: Message)

	/**
	 * 将消息转为当前环境的Text
	 * @param message Message
	 * @return T
	 */
	fun Message.toText(): T

	/**
	 * 转换为最后输出的Text
	 * @receiver Message
	 * @return T
	 */
	fun Message.toFinalText(): T

	/**
	 * 消息预处理
	 *
	 * 消息发送前的处理
	 *
	 * @param message Message
	 * @return Message
	 */
	fun Message.messagePreHandle(player: P): Message

	/**
	 * 向服务器广播消息Text
	 * @param message T
	 */
	fun broadcast(message: Message)

	/**
	 * 向指定玩家发送悄悄话
	 * @param message T
	 */
	fun whisper(message: Message)

	/**
	 * 转换为玩家信息
	 * @param player P
	 * @return PlayerInfo
	 */
	fun P.playerInfo(): PlayerInfo

	/**
	 * 获取服务器信息
	 * @return ServerInfo
	 */
	fun serverInfo(): ServerInfo

	/**
	 * 将玩家信息转换为Text
	 * @param player PlayerInfo
	 * @return T
	 */
	fun PlayerInfo.toText(): T

	/**
	 * 将服务器信息转换为Text
	 * @param serverInfo ServerInfo
	 * @return T
	 */
	fun ServerInfo.toText(): T

	/**
	 * 获取被 @ 的玩家列表
	 * @param message Message
	 * @return List<String>
	 */
	fun Message.getAtList(): List<String>
}