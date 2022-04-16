package org.bakamc.chat.common.websocket

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.websocket

 * 文件名 BakaWSClient

 * 创建时间 2022/4/10 19:27

 * @author forpleuvoir

 */
class BakaWSClient(uri: String, var onMessage: (String) -> Unit) : WebSocketClient(URI.create(uri)) {

	override fun onOpen(handshakedata: ServerHandshake?) {
		println("[BakaChat]服务已连接")
	}

	override fun onMessage(message: String) {
		onMessage.invoke(message)
	}

	override fun onClose(code: Int, reason: String?, remote: Boolean) {
		println("[BakaChat]服务已关闭{code:$code,reason:$reason,remote:$remote}")
	}

	override fun onError(ex: Exception?) {
		println("[BakaChat]服务出错")
		ex?.printStackTrace()
	}
}