package bakamc.chat.common

import java.util.*

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common

 * 文件名 PlayerInfo

 * 创建时间 2022/6/4 17:48

 * @author forpleuvoir

 */
class PlayerInfo(
	/**
	 * 玩家名称
	 */
	val name: String,
	/**
	 * 玩家显示名称
	 */
	val displayName: String,
	/**
	 * 玩家uuid
	 */
	val uuid: UUID,
	/**
	 * 玩家等级
	 */
	val level: Int,
	/**
	 * 玩家经验值
	 */
	val experience: Int,
	/**
	 * 最大生命值
	 */
	val maxHealth: Float,
	/**
	 * 当前生命值
	 */
	val health: Float,
	/**
	 * 所在维度
	 */
	val dimension: String,
) {
	override fun toString(): String {
		return "PlayerInfo(name='$name', displayName='$displayName', uuid=$uuid, level=$level, experience=$experience, maxHealth=$maxHealth, health=$health, dimension='$dimension')"
	}
}