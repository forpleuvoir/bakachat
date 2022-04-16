package org.bakamc.chat.common.message.textevent

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.message.textevent

 * 文件名 ClickEvent

 * 创建时间 2022/4/10 22:03

 * @author forpleuvoir

 */
class ClickEvent(val action: Action, val value: String) : TextEvent() {

	override fun toString(): String {
		return "ClickEvent(action=$action, value='$value')"
	}

	enum class Action {
		OPEN_URL,
		OPEN_FILE,
		RUN_COMMAND,
		SUGGEST_COMMAND,
		CHANGE_PAGE,
		COPY_TO_CLIPBOARD
	}
}

