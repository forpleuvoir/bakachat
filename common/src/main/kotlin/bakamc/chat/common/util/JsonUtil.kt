package bakamc.chat.common.util

import com.google.gson.*

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.util

 * 文件名 JsonUtil

 * 创建时间 2022/6/5 13:52

 * @author forpleuvoir

 */
object JsonUtil {

	val gson: Gson = GsonBuilder().setPrettyPrinting().create()

	fun parseToJsonArray(json: String): JsonArray {
		return JsonParser.parseString(json).asJsonArray
	}

	fun JsonObject.ifNullOr(key: String, or: String): String {
		return if (this[key] != null) this[key].asString else or
	}

	fun JsonObject.ifNullOr(key: String, or: Number): Number {
		return if (this[key] != null) this[key].asNumber else or
	}

	fun JsonObject.ifNullOr(key: String, or: JsonElement): JsonElement {
		return if (this[key] != null) this[key] else or
	}
}