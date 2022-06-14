package bakamc.chat.common.message

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.message

 * 文件名 BakaChatWebSocketClient

 * 创建时间 2022/6/5 14:04

 * @author forpleuvoir

 */
class BakaChatWebSocketClient(uri: String, var onMessage: (String) -> Unit) : WebSocketClient(URI.create(uri)) {

	var onOpen: ((ServerHandshake) -> Unit)? = null

	var onClose: ((code: Int, reason: String, remote: Boolean) -> Unit)? = null

	var onError: ((Exception) -> Unit)? = {
		it.printStackTrace()
	}

	override fun onOpen(handshakedata: ServerHandshake) {
		println("[BakaChat]服务已连接")
		onOpen?.invoke(handshakedata)
	}

	override fun onMessage(message: String) {
		try {
			onMessage.invoke(message)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun onClose(code: Int, reason: String, remote: Boolean) {
		println("[BakaChat]服务已关闭{code:$code,reason:$reason,remote:$remote}")
		onClose?.invoke(code, reason, remote)
	}

	override fun onError(ex: Exception) {
		println("[BakaChat]服务出错")
		onError?.invoke(ex)
	}
}