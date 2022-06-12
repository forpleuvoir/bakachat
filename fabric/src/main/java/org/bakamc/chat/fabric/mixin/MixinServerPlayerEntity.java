package org.bakamc.chat.fabric.mixin;

import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * 项目名 bakachat
 * <p>
 * 包名 org.bakamc.chat.fabric.mixin
 * <p>
 * 文件名 MixinServerPlayerEntity
 * <p>
 * 创建时间 2022/6/12 15:15
 *
 * @author forpleuvoir
 */
@Mixin(ServerPlayerEntity.class)
public interface MixinServerPlayerEntity {

	@Invoker("getMessageTypeId")
	int getMessageTypeId(RegistryKey<MessageType> registryKey);
}
