package org.bakamc.chat.fabric.mixin;

import net.minecraft.server.MinecraftServer;
import org.bakamc.chat.fabric.BakaChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

/**
 * 项目名 bakachat
 * <p>
 * 包名 org.bakamc.chat.fabric.mixin
 * <p>
 * 文件名 MixinMinecraftServer
 * <p>
 * 创建时间 2022/6/12 12:44
 *
 * @author forpleuvoir
 */

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
	@Inject(method = "startServer", at = @At("RETURN"))
	private static void startServer(Function<Thread, MinecraftServer> function, CallbackInfoReturnable<MinecraftServer> cir) {
		BakaChat.server = cir.getReturnValue();
	}
}
