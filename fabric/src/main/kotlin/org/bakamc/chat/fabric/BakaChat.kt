package org.bakamc.chat.fabric

import net.fabricmc.api.ModInitializer
import net.minecraft.server.MinecraftServer

/**
 *

 * 项目名 bakachat

 * 包名 org.bakamc.chat.fabric

 * 文件名 BakaChat

 * 创建时间 2022/6/12 12:38

 * @author forpleuvoir

 */
object BakaChat : ModInitializer {

	const val MOD_ID: String = "bakachat"

	lateinit var server: MinecraftServer

	override fun onInitialize() {
		TODO("Not yet implemented")
	}
}