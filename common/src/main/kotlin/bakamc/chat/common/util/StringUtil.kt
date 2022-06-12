package bakamc.chat.common.util

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.util

 * 文件名 StringUtil

 * 创建时间 2022/6/5 13:57

 * @author forpleuvoir

 */

fun String.replace(map: Map<String, String>): String {
	var temp: String = this
	map.forEach { (k, v) ->
		temp = temp.replace(k, v)
	}
	return temp
}