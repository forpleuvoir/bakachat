package bakamc.chat.common.message

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.message

 * 文件名 MessageType

 * 创建时间 2022/6/4 18:13

 * @author forpleuvoir

 */
enum class MessageType(val remark: String) {

	CHAT("普通聊天消息"), WHISPER("悄悄话");

}