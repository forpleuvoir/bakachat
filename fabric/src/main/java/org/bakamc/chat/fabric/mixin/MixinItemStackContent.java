package org.bakamc.chat.fabric.mixin;

import com.google.gson.JsonElement;
import net.minecraft.text.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * 项目名 bakachat
 * <p>
 * 包名 org.bakamc.chat.fabric.mixin
 * <p>
 * 文件名 MixinHoverEvent
 * <p>
 * 创建时间 2022/4/16 22:18
 *
 * @author forpleuvoir
 */
@Mixin(HoverEvent.ItemStackContent.class)
public interface MixinItemStackContent {

	@Invoker("parse")
	static HoverEvent.ItemStackContent parseItemStackContent(JsonElement jsonElement) {
		throw new AssertionError();
	}
}
