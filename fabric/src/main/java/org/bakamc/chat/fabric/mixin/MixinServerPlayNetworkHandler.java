package org.bakamc.chat.fabric.mixin;

import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bakamc.chat.fabric.BakaChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 项目名 bakachat
 * <p>
 * 包名 org.bakamc.chat.fabric.mixin
 * <p>
 * 文件名 MixinServerPlayNetworkHandler
 * <p>
 * 创建时间 2022/4/16 23:05
 *
 * @author forpleuvoir
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler {

	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "handleMessage", at = @At("HEAD"), cancellable = true)
	public void handleMessage(TextStream.Message arg, CallbackInfo ci) {
		if (!arg.getRaw().startsWith("/"))
			BakaChat.hasMessageHandler(messageHandler -> {
				messageHandler.sendMessage(this.player, arg.getRaw(), null, null);
				ci.cancel();
			});
	}
}
