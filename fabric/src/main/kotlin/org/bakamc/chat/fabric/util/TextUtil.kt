package org.bakamc.chat.fabric.util

import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText

/**
 *

 * 项目名 bakachat

 * 包名 bakamc.chat.common.util

 * 文件名 TextUtil

 * 创建时间 2022/6/15 0:39

 * @author forpleuvoir

 */

val String.text: MutableText
	get() = MutableText.of(LiteralTextContent(this))


