package org.bakamc.chat.common.util

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.common.util

 * 文件名 StringUtil

 * 创建时间 2022/4/16 12:48

 * @author forpleuvoir

 */


fun String.replace(map: Map<String, String>): String {
	var temp: String = this
	map.forEach { (k, v) ->
		temp = temp.replace(k, v)
	}
	return temp
}


