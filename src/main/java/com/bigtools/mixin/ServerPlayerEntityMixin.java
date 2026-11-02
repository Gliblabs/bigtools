package com.bigtools.mixin;

import com.bigtools.ToolRackHolder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Carry the tool rack over on respawn / dimension change, same rules as the normal inventory. */
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void bigtools$copyRack(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
		boolean keep = alive
			|| self.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)
			|| oldPlayer.isSpectator();
		if (!keep) return;
		SimpleInventory from = ((ToolRackHolder) (Object) oldPlayer).bigtools_getRack();
		SimpleInventory to = ((ToolRackHolder) (Object) self).bigtools_getRack();
		for (int i = 0; i < from.size(); i++) {
			to.setStack(i, from.getStack(i).copy());
		}
	}
}
