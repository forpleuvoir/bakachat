package org.bakamc.chat.common.util


/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.util

 * 文件名 MessageUtil

 * 创建时间 2022/4/13 22:00

 * @author forpleuvoir

 */
object MessageUtil {

	private const val PLACEHOLDER = "&"

	private val map = mapOf(
		"${PLACEHOLDER}4" to "§4",
		"${PLACEHOLDER}c" to "§c",
		"${PLACEHOLDER}6" to "§6",
		"${PLACEHOLDER}e" to "§e",
		"${PLACEHOLDER}2" to "§2",
		"${PLACEHOLDER}a" to "§a",
		"${PLACEHOLDER}b" to "§b",
		"${PLACEHOLDER}3" to "§3",
		"${PLACEHOLDER}1" to "§1",
		"${PLACEHOLDER}9" to "§9",
		"${PLACEHOLDER}d" to "§d",
		"${PLACEHOLDER}5" to "§5",
		"${PLACEHOLDER}f" to "§f",
		"${PLACEHOLDER}7" to "§7",
		"${PLACEHOLDER}8" to "§8",
		"${PLACEHOLDER}0" to "§0",
		"${PLACEHOLDER}k" to "§k",
		"${PLACEHOLDER}l" to "§l",
		"${PLACEHOLDER}m" to "§m",
		"${PLACEHOLDER}n" to "§n",
		"${PLACEHOLDER}o" to "§o",
		"${PLACEHOLDER}r" to "§r",
	)

	fun handleFormat(message: String): String {
		return message.replace(map)
	}

	fun reHandleFormat(message: String): String {
		var temp: String = message
		map.forEach { (k, v) ->
			temp = temp.replace(v, k)
		}
		return temp
	}

	fun cleanFormatting(message: String): String {
		var temp: String = message
		map.forEach { (k, v) ->
			temp = temp.replace(v, "")
		}
		return temp
	}
}